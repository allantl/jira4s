package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class IssueType(
    self: String,
    id: String,
    description: Option[String],
    name: String,
    iconUrl: String,
    subtask: Boolean
)

object IssueType {
  implicit val encoder: Encoder[IssueType] = deriveEncoder
  implicit val decoder: Decoder[IssueType] = deriveDecoder
}
