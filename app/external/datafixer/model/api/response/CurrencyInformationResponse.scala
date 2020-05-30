package external.datafixer.model.api.response

case class CurrencyInformationResponse(success: Boolean,
                                       base: Option[String],
                                       rates: Option[Map[String, Double]],
                                       error: Option[Error])

case class Error(code: Int)
