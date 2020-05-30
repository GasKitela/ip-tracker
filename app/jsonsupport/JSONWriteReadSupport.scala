package jsonsupport

import hangouts._
import external.restcountries.model.api.response.{CountryInformationResponse, Currency, Language}
import external.datafixer.model.api.response.{CurrencyInformationResponse, Error}
import external.distance24.model.DistanceResponse
import external.ip2country.model.api.response.IpInformationResponse
import model.IpServiceMetrics
import model.api.response.{CurrencyResponse, IpMetricsResponse, IpTrackerResponse}
import model.errors._
import org.json4s.DefaultFormats
import org.mongodb.scala.bson.ObjectId
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}

trait JSONWriteReadSupport {

  implicit val config = JsonConfiguration(SnakeCase)

  implicit val json4sFormats = DefaultFormats

  implicit val httpErrorFormat = Json.format[HttpClientError]

  implicit val ipInformationResponseFormat = Json.format[IpInformationResponse]

  implicit val currencyFormat = Json.format[Currency]

  implicit val languageFormat = Json.format[Language]

  implicit val countryInformationResponseFormat = Json.format[CountryInformationResponse]

  implicit val errorFormat = Json.format[Error]

  implicit val currencyInformationResponseFormat = Json.format[CurrencyInformationResponse]

  implicit val currencyResponseFormat = Json.format[CurrencyResponse]

  implicit val ipTrackerResponseformat = Json.format[IpTrackerResponse]

  implicit val distanceResponseFormat = Json.format[DistanceResponse]

  implicit val ipServiceMetricsFormat = Json.format[IpMetricsResponse]

}
