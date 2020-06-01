package external.distance24.client

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import external.datafixer.model.api.response.CurrencyInformationResponse
import external.distance24.model.DistanceResponse
import javax.inject.Inject
import jsonsupport.JSONWriteReadSupport
import model.errors.HttpClientError
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

class Distance24Client @Inject()(config: Configuration)(implicit ec: ExecutionContext) extends JSONWriteReadSupport {

  implicit val backend = AkkaHttpBackend()
  implicit val formats = DefaultFormats
  implicit val serialization = Serialization

  val protocol = config.get[String]("external.distance24.protocol")
  val host = config.get[String]("external.distance24.host")
  val path = "/route.json?stops=Buenos%20Aires|"
  val timeout = config.get[Int]("external.distance24.timeoutinsecs")

  def getDistanceToBsAs(city: String): Either[HttpClientError, DistanceResponse] = {

    val uri = s"$protocol://$host$path$city"

    val request = sttp
      .contentType("application/json")
      .get(uri"$uri")
      .response(asString)
      .send()

    Try(Await.result(request, Duration(timeout, "seconds"))) match {
      case Success(response) => response.body match {
        case Left(r) => Left(HttpClientError(response.code, Option(r)))
        case Right(body) => {
          val jsBody = parse(body).camelizeKeys
          Right(jsBody.extract[DistanceResponse])
        }
      }
      case Failure(r) => Left(HttpClientError(500, Option(r.toString)))
    }
  }

}
