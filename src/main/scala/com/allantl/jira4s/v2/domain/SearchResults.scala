package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SearchResults(
    expand: String,
    startAt: Int,
    maxResults: Int,
    total: Int,
    issues: List[Issue],
    warningMessages: Option[List[String]] = None
)

object SearchResults {
  implicit val decoder: Decoder[SearchResults] = deriveDecoder
  implicit val encoder: Encoder[SearchResults] = deriveEncoder
}
