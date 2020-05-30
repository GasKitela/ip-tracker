package services

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import external.datafixer.facade.DataFixerFacade
import javax.inject.{Inject, Singleton}
import external.distance24.facade.Distance24Facade
import external.ip2country.facade.Ip2CountryFacade
import external.restcountries.facade.RestCountriesFacade
import mapper.IpTrackerMapper
import model.api.response.{IpMetricsResponse, IpTrackerResponse}
import model.errors.{HttpClientError, MongoError}
import model.{CountryInformation, CurrencyRates, Distance, IpLocation}
import org.mongodb.scala.Completed
import repository.IpServiceInfoRepository

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class IpInformationService @Inject()(ip2CountryFacade: Ip2CountryFacade,
                                     restCountriesFacade: RestCountriesFacade,
                                     dataFixerFacade: DataFixerFacade,
                                     distance24Facade: Distance24Facade,
                                     mapper: IpTrackerMapper,
                                     ipServiceInfoRepository: IpServiceInfoRepository) {

  def getIpInformation(ip: String): Either[HttpClientError, IpLocation] = {
    ip2CountryFacade.getIpGeoLocationInfo(ip)
  }

  def getCountryInformation(countryName: String): Either[HttpClientError, CountryInformation] = {
    restCountriesFacade.getCountryInfo(countryName)
  }

  def getCurrencyRates(currency: String): Either[HttpClientError, CurrencyRates] = {
    dataFixerFacade.getCurrencyRates(currency)
  }

  def getDistanceFromBsAsTo(capital: String): Either[HttpClientError, Distance] = {
    distance24Facade.getDistanceToBsAs(capital)
  }

  def getMinRequestDistance: Future[IpMetricsResponse] = {
    ipServiceInfoRepository.findMinDistance.map(r => mapper.fromDbToApi(r))
      .recoverWith { case t => Future.failed(MongoError(t.toString)) }
  }

  def getMaxRequestDistance: Future[IpMetricsResponse] = {
    ipServiceInfoRepository.findMaxDistance.map(r => mapper.fromDbToApi(r))
      .recoverWith { case t => Future.failed(MongoError(t.toString)) }
  }

  def getAverageRequestDistance: Future[Int] = {
    ipServiceInfoRepository.findAll.map(results => results.map(elem => elem.distance).sum / results.size)
      .recoverWith { case t => Future.failed(MongoError(t.toString)) }
  }

  def saveMetrics(ip: String, countryName: String, distance: Int): Future[Completed] = {
    ipServiceInfoRepository.insertMetrics(ip, countryName, distance)
  }

  def mapToIpTrackerResponse(ipLocation: IpLocation, countryInfo: CountryInformation,
                             currencyRates: CurrencyRates, distance: Distance): IpTrackerResponse = {
    mapper.fromApiResponsesToIpTrackerResponse(ipLocation, countryInfo, currencyRates, distance)
  }

  def getAllByIp(ip: String): Either[HttpClientError, IpTrackerResponse] = for {

    ipInfo <- getIpInformation(ip)

    countryInfo <- getCountryInformation(ipInfo.country)

    currencyInfo <- getCurrencyRates("EUR")

    distanceInfo <- getDistanceFromBsAsTo(countryInfo.capital)

    _ = saveMetrics(ipInfo.ip, ipInfo.country, distanceInfo.distanceInKm)

  } yield mapToIpTrackerResponse(ipInfo, countryInfo, currencyInfo, distanceInfo)
}
