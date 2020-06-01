package services

import java.util.concurrent.TimeUnit

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
import play.api.Configuration

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class IpInformationService @Inject()(config: Configuration,
                                     ip2CountryFacade: Ip2CountryFacade,
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

  def getMinRequestDistance(implicit ec: ExecutionContext): Future[IpMetricsResponse] = {
    ipServiceInfoRepository.findMinDistance.map(r => mapper.fromDbToApi(r))
      .recoverWith { case t => Future.failed(MongoError(t.toString)) }
  }

  def getMaxRequestDistance(implicit ec: ExecutionContext): Future[IpMetricsResponse] = {
    ipServiceInfoRepository.findMaxDistance.map(r => mapper.fromDbToApi(r))
      .recoverWith { case t => Future.failed(MongoError(t.toString)) }
  }

  def getAverageRequestDistance(implicit ec: ExecutionContext): Future[Int] = {
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

    currencyInfo <- getCurrencyRates(countryInfo.getCurrency)

    distanceInfo <- getDistanceFromBsAsTo(countryInfo.capital)

    _ = saveMetrics(ipInfo.ip, ipInfo.country, distanceInfo.distanceInKm)

  } yield mapToIpTrackerResponse(ipInfo, countryInfo, currencyInfo, distanceInfo)
}
