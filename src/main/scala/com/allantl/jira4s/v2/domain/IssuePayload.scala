package com.allantl.jira4s.v2.domain

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

case class IssuePayload(
    fields: Seq[IssueField],
    properties: Option[Seq[EntityProperty]] = None,
    update: Option[Json] = None
) {
  def ++(additionalFields: Seq[IssueField]): IssuePayload =
    copy(fields = fields ++ additionalFields)
  def ++(field: IssueField): IssuePayload = copy(fields = field +: fields)
  def ++(property: EntityProperty): IssuePayload =
    copy(properties = properties.map(_ :+ property).orElse(Some(Seq(property))))
}

object IssuePayload {

  def apply(fields: IssueField*): IssuePayload =
    IssuePayload(fields = fields)

  def apply(field: IssueField): IssuePayload =
    IssuePayload(Seq(field))

  implicit val issuePayloadEncoder: Encoder[IssuePayload] = new Encoder[IssuePayload] {
    override def apply(p: IssuePayload) = {

      def convertField(field: IssueField): (String, Json) = field match {
        case IssueField(fieldId, fieldValue) => fieldId -> fieldValue
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
          .right
          .map(_.toSeq.map {
            case (key, value) =>
              IssueField(key, value)
          })
          .right
        properties <- c.downField("properties").as[Option[Seq[EntityProperty]]].right
        update <- c.downField("update").as[Option[Json]].right
      } yield IssuePayload(fields, properties, update)
  }
}

case class IssueField(id: String, value: Json)

object IssueField {
  def apply(id: String, value: String): IssueField = IssueField(id, Json.fromString(value))

  def apply(id: String, value: Long): IssueField = IssueField(id, Json.fromLong(value))

  def apply(id: String, value: Int): IssueField = IssueField(id, Json.fromInt(value))

  def apply(id: String, value: Boolean): IssueField = IssueField(id, Json.fromBoolean(value))

  def apply(id: String, value: (String, String)): IssueField =
    IssueField(id, Json.obj((value._1, Json.fromString(value._2))))

  implicit val fieldEncoder: Encoder[IssueField] = deriveEncoder
  implicit val fieldDecoder: Decoder[IssueField] = deriveDecoder
}

case class IssueCreateResponse(id: String, key: String, self: String)

object IssueCreateResponse {
  implicit val issueCreateResponseEncoder: Encoder[IssueCreateResponse] = deriveEncoder
  implicit val issueCreateResponseDecoder: Decoder[IssueCreateResponse] = deriveDecoder
}
