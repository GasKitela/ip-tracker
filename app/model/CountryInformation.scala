package model

case class CountryInformation(country: String,
                              capital: String,
                              timezones: List[String],
                              currencies: List[String],
                              languages: List[String])
