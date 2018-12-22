package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Field(
    id: String,
    key: String,
    name: String,
    custom: Boolean,
    orderable: Boolean,
    navigable: Boolean,
    searchable: Boolean,
    clauseNames: List[String],
    schema: FieldSchema
)

object Field {
  implicit val encoder: Encoder[Field] = deriveEncoder
  implicit val decoder: Decoder[Field] = deriveDecoder
}

case class FieldSchema(
    `type`: String,
    system: Option[String] = None,
    items: Option[String] = None,
    custom: Option[String] = None,
    customId: Option[Long] = None
)

object FieldSchema {
  implicit val fieldSchemaEncoder: Encoder[FieldSchema] = deriveEncoder
  implicit val fieldSchemaDecoder: Decoder[FieldSchema] = deriveDecoder
}
