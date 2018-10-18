package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto._

case class Issue(
    id: String,
    self: String,
    key: String,
    fields: Json,
    properties: Option[Json] = None,
    renderedFields: Option[Json] = None
)

object Issue {
  implicit val issueDecoder: Decoder[Issue] = deriveDecoder
  implicit val issueEncoder: Encoder[Issue] = deriveEncoder
}
