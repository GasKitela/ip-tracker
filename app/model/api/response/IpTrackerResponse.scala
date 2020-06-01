package model.api.response

case class IpTrackerResponse(ip: String,
                             date: String,
                             country: String,
                             isoCode: String,
                             capital: String,
                             languages: List[String],
                             currency: CurrencyResponse,
                             hours: List[String],
                             distance: Int)

case class CurrencyResponse(currency: String,
                            usdRate: Option[Double])