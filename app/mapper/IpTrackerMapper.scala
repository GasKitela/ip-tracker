package mapper

import javax.inject.Inject
import model.{CountryInformation, CurrencyRates, Distance, IpLocation, IpServiceMetrics}
import model.api.response.{CurrencyResponse, IpMetricsResponse, IpTrackerResponse}
import org.joda.time.DateTime

class IpTrackerMapper @Inject()() {

  def fromApiResponsesToIpTrackerResponse(ipLocation: IpLocation, countryInfo: CountryInformation,
                                          currencyRates: CurrencyRates, distanceResponse: Distance) = {
    IpTrackerResponse(ipLocation.ip,
                      DateTime.now().toString("MM/dd/yyyy HH:mm:ss"),
                      countryInfo.country,
                      ipLocation.countryCode,
                      countryInfo.languages,
                      CurrencyResponse(currencyRates.base, currencyRates.rates.get("uSD")),
                      countryInfo.timezones,
                      distanceResponse.distanceInKm)
  }

  def fromDbToApi(ipServiceMetrics: IpServiceMetrics) = IpMetricsResponse(ipServiceMetrics.ip, ipServiceMetrics.country, ipServiceMetrics.distance)
}
