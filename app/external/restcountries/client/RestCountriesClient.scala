package external.restcountries.client

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import javax.inject.Inject
import jsonsupport.JSONWriteReadSupport
import external.restcountries.model.api.response.CountryInformationResponse
import model.errors.HttpClientError
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization
import play.api.libs.ws.WSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

class RestCountriesClient @Inject()(ws: WSClient)(implicit ec: ExecutionContext) extends JSONWriteReadSupport {

  implicit val backend = AkkaHttpBackend()
  implicit val formats = DefaultFormats
  implicit val serialization = Serialization

  val host = "restcountries.eu"
  val path = "/rest/v2/name"

  def getCountryInfo(countryName: String): Either[HttpClientError, List[CountryInformationResponse]] = {

    val uri = s"https://$host$path/$countryName"

    val request = sttp
      .contentType("application/json")
      .get(uri"$uri")
      .response(asString)
      .send()

    Try(Await.result(request, Duration(20, "seconds"))) match {
      case Success(response) => response.body match {
        case Left(r) => Left(HttpClientError(response.code, Option(r)))
        case Right(body) => {
          val jsBody = parse(body).camelizeKeys
          Right(jsBody.extract[List[CountryInformationResponse]])
        }
      }
      case Failure(r) => Left(HttpClientError(500, Option(r.toString)))
    }
  }

}
