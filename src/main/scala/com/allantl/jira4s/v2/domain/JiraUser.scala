package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class JiraUser(
    self: String,
    name: String,
    key: String,
    accountId: String,
    emailAddress: Option[String] = None,
    avatarUrls: Option[AvatarUrls] = None,
    displayName: String,
    active: Boolean,
    timeZone: Option[String] = None,
    groups: Option[Groups] = None,
    applicationRoles: Option[ApplicationRoles] = None
)

object JiraUser {
  implicit val decoder: Decoder[JiraUser] = deriveDecoder
  implicit val encoder: Encoder[JiraUser] = deriveEncoder
}

case class AvatarUrls(
    `16x16`: Option[String],
    `24x24`: Option[String],
    `32x32`: Option[String],
    `48x48`: Option[String]
)

object AvatarUrls {
  implicit val decoder: Decoder[AvatarUrls] = deriveDecoder
  implicit val encoder: Encoder[AvatarUrls] = deriveEncoder
}

case class Groups(size: Int, items: List[GroupItem])

object Groups {
  implicit val decoder: Decoder[Groups] = deriveDecoder
  implicit val encoder: Encoder[Groups] = deriveEncoder
}

case class GroupItem(name: String, self: String)

object GroupItem {
  implicit val decoder: Decoder[GroupItem] = deriveDecoder
  implicit val encoder: Encoder[GroupItem] = deriveEncoder
}

case class ApplicationRoles(size: Int, items: List[ApplicationRoleItem])

object ApplicationRoles {
  implicit val decoder: Decoder[ApplicationRoles] = deriveDecoder
  implicit val encoder: Encoder[ApplicationRoles] = deriveEncoder
}

case class ApplicationRoleItem(key: String, name: String)

object ApplicationRoleItem {
  implicit val decoder: Decoder[ApplicationRoleItem] = deriveDecoder
  implicit val encoder: Encoder[ApplicationRoleItem] = deriveEncoder
}
