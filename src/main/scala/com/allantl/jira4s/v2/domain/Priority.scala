package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Priority(
    self: String,
    statusColor: String,
    description: String,
    iconUrl: String,
    name: String,
    id: String
)

object Priority {
  implicit val encoder: Encoder[Priority] = deriveEncoder
  implicit val decoder: Decoder[Priority] = deriveDecoder
}
