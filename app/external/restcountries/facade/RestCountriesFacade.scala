package external.restcountries.facade

import external.restcountries.client.RestCountriesClient
import javax.inject.Inject
import model.CountryInformation
import model.errors.HttpClientError

class RestCountriesFacade @Inject()(restCountriesClient: RestCountriesClient) {

  def getCountryInfo(country: String) = {
    restCountriesClient.getCountryInfo(country) match {
      case Right(x :: _) => Right(CountryInformation(x.name,
                                    x.capital,
                                    x.timezones,
                                    x.currencies.map(_.code),
                                    x.languages.map(_.name)))
      case Right(Nil) => Left(HttpClientError())
      case Left(err) => Left(err)
    }
  }

}
