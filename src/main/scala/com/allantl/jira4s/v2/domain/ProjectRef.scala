package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class ProjectRef(self: String, id: Long, key: String)

object ProjectRef {
  implicit val encoder: Encoder[ProjectRef] = deriveEncoder
  implicit val decoder: Decoder[ProjectRef] = deriveDecoder
}
