package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class Issue(id: String)

object Issue {
  implicit val issueDecoder: Decoder[Issue] = deriveDecoder
  implicit val issueEncoder: Encoder[Issue] = deriveEncoder
}
