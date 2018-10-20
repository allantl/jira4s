package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto._

case class EntityProperty(key: String, value: Json)

object EntityProperty {
  implicit val entityPropertyEncoder: Encoder[EntityProperty] = deriveEncoder
  implicit val entityPropertyDecoder: Decoder[EntityProperty] = deriveDecoder
}
