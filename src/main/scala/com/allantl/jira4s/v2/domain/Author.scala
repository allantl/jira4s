package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Author(
                   self: String,
                   name: String,
                   key: String,
                   emailAddress: String,
                   displayName: String,
                   active: Boolean,
                   timeZone: String
                 )

object Author {
  implicit val issueDecoder: Decoder[Author] = deriveDecoder
  implicit val issueEncoder: Encoder[Author] = deriveEncoder
}
