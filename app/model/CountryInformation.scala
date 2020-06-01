package model

import org.joda.time.{DateTime, DateTimeZone}

case class CountryInformation(country: String,
                              capital: String,
                              timezones: List[String],
                              currencies: List[String],
                              languages: List[String]) {

  def getCurrency = currencies match {
    case x :: _ => x
    case _ => ""
  }

  def getHoursForTimezones = timezones.map{ tz =>
    val hour = DateTime.now(DateTimeZone.forID(tz.substring(3))).toString("HH:mm:ss")
    s"$hour ($tz)"
  }

}
