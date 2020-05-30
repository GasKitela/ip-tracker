package external.ip2country.facade

import external.ip2country.client.Ip2CountryClient
import javax.inject.Inject
import model.IpLocation

class Ip2CountryFacade @Inject()(ip2CountryClient: Ip2CountryClient) {

  def getIpGeoLocationInfo(ip: String) = {
    ip2CountryClient.getIpGeoLocationInfo(ip).map(r => IpLocation(ip, r.countryCode3, r.countryName))
  }

}
