package external.datafixer.client

import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.{asString, sttp, _}
import javax.inject.Inject
import jsonsupport.JSONWriteReadSupport
import external.datafixer.model.api.response.CurrencyInformationResponse
import model.errors.HttpClientError
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

class DataFixerClient @Inject()(config: Configuration)(implicit ec: ExecutionContext) extends JSONWriteReadSupport {

  implicit val backend = AkkaHttpBackend()
  implicit val formats = DefaultFormats
  implicit val serialization = Serialization

  val protocol = config.get[String]("external.datafixer.protocol")
  val host = config.get[String]("external.datafixer.host")
  val path = "/api/latest"
  val apiKey = config.get[String]("external.datafixer.apikey")
  val timeout = config.get[Int]("external.datafixer.timeoutinsecs")

  def getCurrencyRates(currency: String): Either[HttpClientError, CurrencyInformationResponse] = {

    val uri = s"$protocol://$host$path?access_key=$apiKey&base=$currency&symbols=USD"

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
          Right(jsBody.extract[CurrencyInformationResponse])
        }
      }
      case Failure(r) => Left(HttpClientError(500, Option(r.toString)))
    }
  }

}
