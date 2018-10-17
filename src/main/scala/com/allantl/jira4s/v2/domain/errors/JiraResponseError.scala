package com.allantl.jira4s.v2.domain.errors

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class JiraResponseError(errorMessages: Seq[String], errors: Json)

object JiraResponseError {
  implicit val jiraResponseDecoder: Decoder[JiraResponseError] = deriveDecoder
  implicit val jiraResponseEncoder: Encoder[JiraResponseError] = deriveEncoder
}
