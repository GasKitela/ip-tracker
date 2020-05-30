package external.restcountries.model.api.response

case class CountryInformationResponse(name: String,
                                      capital: String,
                                      timezones: List[String],
                                      currencies: List[Currency],
                                      languages: List[Language])

case class Currency(code: String,
                    name: String)

case class Language(name: String)