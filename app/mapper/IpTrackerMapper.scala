package mapper

import javax.inject.Inject
import model.api.response.{CurrencyResponse, IpMetricsResponse, IpTrackerResponse}
import model._
import org.joda.time.DateTime

class IpTrackerMapper @Inject()() {

  def fromApiResponsesToIpTrackerResponse(ipLocation: IpLocation, countryInfo: CountryInformation,
                                          currencyRates: CurrencyRates, distanceResponse: Distance) = {
    IpTrackerResponse(ipLocation.ip,
                      DateTime.now().toString("MM/dd/yyyy HH:mm:ss"),
                      countryInfo.country,
                      ipLocation.countryCode,
                      countryInfo.capital,
                      countryInfo.languages,
                      CurrencyResponse(currencyRates.base, currencyRates.rates.get("uSD")),
                      countryInfo.getHoursForTimezones,
                      distanceResponse.distanceInKm)
  }

  def fromDbToApi(ipServiceMetrics: IpServiceMetrics) = IpMetricsResponse(ipServiceMetrics.ip, ipServiceMetrics.country, ipServiceMetrics.distance)
}
