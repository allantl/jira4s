package com.allantl.jira4s.v2.domain.enums

import cats.Show

sealed trait ValidateQuery

object ValidateQuery {

  case object Strict extends ValidateQuery
  case object Warn extends ValidateQuery
  case object None extends ValidateQuery
  case object True extends ValidateQuery
  case object False extends ValidateQuery

  implicit val show: Show[ValidateQuery] = Show.show {
    case Strict => "strict"
    case Warn => "warn"
    case None => "none"
    case True => "true"
    case False => "false"
  }
}
