package com.allantl.jira4s.v2.domain.errors

abstract class JiraError(msg: String) extends Exception(msg) {
  def errMsg: String = msg
}

case object JsonDeserializationError extends JiraError("Unable to parse JSON")
case class AccessDeniedError(msg: String) extends JiraError(msg)
case class UnauthorizedError(msg: String) extends JiraError(msg)
case class ResourceNotFound(msg: String) extends JiraError(msg)
case class GenericError(msg: String) extends JiraError(msg)
