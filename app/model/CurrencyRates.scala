package model

case class CurrencyRates(base: String,
                         rates: Map[String, Double])
