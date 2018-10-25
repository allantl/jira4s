package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, HCursor, Json}

case class Changelog(startAt: Int, maxResults: Int, total: Int, histories: List[History])

object Changelog {
  implicit val decoder: Decoder[Changelog] = deriveDecoder
  implicit val encoder: Encoder[Changelog] = deriveEncoder
}

case class History(id: String, author: Author, created: String, items: List[HistoryItem])

object History {
  implicit val decoder: Decoder[History] = deriveDecoder
  implicit val encoder: Encoder[History] = deriveEncoder
}

case class HistoryItem(
    field: String,
    fieldType: String,
    fieldId: String,
    from: Option[String],
    fromString: Option[String],
    to: Option[String],
    tostring: Option[String])

object HistoryItem {

  implicit val encoder: Encoder[HistoryItem] = (hi: HistoryItem) =>
    Json.obj(
      ("field", Json.fromString(hi.field)),
      ("fieldtype", Json.fromString(hi.fieldType)),
      ("fieldId", Json.fromString(hi.fieldId)),
      ("from", hi.from.fold(Json.Null)(Json.fromString)),
      ("fromString", hi.fromString.fold(Json.Null)(Json.fromString)),
      ("to", hi.to.fold(Json.Null)(Json.fromString)),
      ("toString", hi.to.fold(Json.Null)(Json.fromString))
  )

  implicit val decoder: Decoder[HistoryItem] = (c: HCursor) =>
    for {
      field <- c.downField("field").as[String]
      fieldType <- c.downField("fieldtype").as[String]
      fieldId <- c.downField("fieldId").as[String]
      from <- c.downField("from").as[Option[String]]
      fromString <- c.downField("fromString").as[Option[String]]
      to <- c.downField("to").as[Option[String]]
      toString <- c.downField("toString").as[Option[String]]
    } yield
      HistoryItem(
        field,
        fieldType,
        fieldId,
        from,
        fromString,
        to,
        toString
    )
}

case class Author(
    self: String,
    name: String,
    key: String,
    accountId: String,
    emailAddress: String,
    avatarUrls: Json,
    displayName: String,
    active: Boolean,
    timeZone: String
)

object Author {
  implicit val decoder: Decoder[Author] = deriveDecoder
  implicit val encoder: Encoder[Author] = deriveEncoder
}
