package model.api.response

case class IpTrackerResponse(ip: String,
                             date: String,
                             country: String,
                             isoCode: String,
                             languages: List[String],
                             currency: CurrencyResponse,
                             timeZones: List[String],
                             distance: Int)

case class CurrencyResponse(currency: String,
                            usdRate: Option[Double])