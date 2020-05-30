package model.errors

case class HttpClientError(code: Int = 500, msg: Option[String] = None) extends Throwable

case class MongoError(description: String) extends Throwable