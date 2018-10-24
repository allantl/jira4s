package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class IssueProperty(key: String, value: Json)

object IssueProperty {
  implicit val issuePropertyDecoder: Decoder[IssueProperty] = deriveDecoder
  implicit val issuePropertyEncoder: Encoder[IssueProperty] = deriveEncoder
}
