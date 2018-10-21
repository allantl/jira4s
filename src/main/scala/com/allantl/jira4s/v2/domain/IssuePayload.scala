package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

case class IssuePayload(
    fields: Seq[Field],
    properties: Option[Seq[EntityProperty]] = None,
    update: Option[Json] = None
) {
  def ++(additionalFields: Seq[Field]): IssuePayload = copy(fields = fields ++ additionalFields)
  def ++(field: Field): IssuePayload = copy(fields = field +: fields)
  def ++(property: EntityProperty): IssuePayload =
    copy(properties = properties.map(_ :+ property).orElse(Some(Seq(property))))
}

object IssuePayload {

  def apply(fields: Field*): IssuePayload =
    IssuePayload(fields = fields)

  def apply(field: Field): IssuePayload =
    IssuePayload(Seq(field))

  implicit val issuePayloadEncoder: Encoder[IssuePayload] = new Encoder[IssuePayload] {
    override def apply(p: IssuePayload) = {

      def convertField(field: Field): (String, Json) = field match {
        case Field(fieldId, fieldValue) => fieldId -> fieldValue
      }
      val fieldsJson = Json.obj(p.fields.map(convertField): _*)

      Json.obj(
        "fields" -> fieldsJson,
        "properties" -> p.properties.asJson,
        "update" -> p.update.asJson
      )
    }
  }

  implicit val issuePayloadDecoder: Decoder[IssuePayload] = new Decoder[IssuePayload] {
    override def apply(c: HCursor) =
      for {
        fields <- c
          .downField("fields")
          .as[Map[String, Json]]
          .map(_.toSeq.map {
            case (key, value) =>
              Field(key, value)
          })
        properties <- c.downField("properties").as[Option[Seq[EntityProperty]]]
        update <- c.downField("update").as[Option[Json]]
      } yield IssuePayload(fields, properties, update)
  }
}

case class Field(id: String, value: Json)

object Field {
  def apply(id: String, value: String): Field = Field(id, Json.fromString(value))

  def apply(id: String, value: Long): Field = Field(id, Json.fromLong(value))

  def apply(id: String, value: Int): Field = Field(id, Json.fromInt(value))

  def apply(id: String, value: Boolean): Field = Field(id, Json.fromBoolean(value))

  def apply(id: String, value: (String, String)): Field =
    Field(id, Json.obj((value._1, Json.fromString(value._2))))

  implicit val fieldEncoder: Encoder[Field] = deriveEncoder
  implicit val fieldDecoder: Decoder[Field] = deriveDecoder
}

case class IssueCreateResponse(id: String, key: String, self: String)

object IssueCreateResponse {
  implicit val issueCreateResponseEncoder: Encoder[IssueCreateResponse] = deriveEncoder
  implicit val issueCreateResponseDecoder: Decoder[IssueCreateResponse] = deriveDecoder
}
