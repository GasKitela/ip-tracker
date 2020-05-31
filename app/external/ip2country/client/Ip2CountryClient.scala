package external.ip2country.client

import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.{asString, sttp, _}
import javax.inject.Inject
import jsonsupport.JSONWriteReadSupport
import external.ip2country.model.api.response.IpInformationResponse
import model.errors.HttpClientError
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

class Ip2CountryClient @Inject()(config: Configuration, ws: WSClient)(implicit ec: ExecutionContext) extends JSONWriteReadSupport {

  implicit val backend = AkkaHttpBackend()
  implicit val formats = DefaultFormats
  implicit val serialization = Serialization

  val protocol = config.get[String]("external.ip2country.protocol")
  val host = config.get[String]("external.ip2country.host")
  val timeout = config.get[Int]("external.ip2country.timeoutinsecs")

  def getIpGeoLocationInfo(ip: String): Either[HttpClientError, IpInformationResponse] = {

    val request = sttp
      .contentType("application/json")
      .get(uri"$protocol://$host/ip?$ip")
      .response(asString)
      .send()

    Try(Await.result(request, Duration(timeout, "seconds"))) match {
      case Success(response) => response.body match {
        case Left(r) => Left(HttpClientError(response.code, Option(r)))
        case Right(body) => {
          val jsBody = parse(body).camelizeKeys
          Right(jsBody.extract[IpInformationResponse])
        }
      }
      case Failure(r) => Left(HttpClientError(500, Option(r.toString)))
    }
  }

}
