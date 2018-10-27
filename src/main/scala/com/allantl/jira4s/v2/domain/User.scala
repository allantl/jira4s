package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class User(
    self: String,
    name: String,
    key: String,
    accountId: String,
    emailAddress: Option[String] = None,
    avatarUrls: Option[Json] = None,
    displayName: String,
    active: Boolean,
    timeZone: Option[String] = None
)

object User {
  implicit val decoder: Decoder[User] = deriveDecoder
  implicit val encoder: Encoder[User] = deriveEncoder
}
