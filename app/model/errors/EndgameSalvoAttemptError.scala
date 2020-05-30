package model.errors


case class EndgameSalvoAttemptError(salvo: Map[String, String]) extends Throwable
