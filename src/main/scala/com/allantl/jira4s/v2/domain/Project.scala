package com.allantl.jira4s.v2.domain

import com.allantl.jira4s.v2.domain.LeadInput.{LeadAccountId, LeadName}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

import scala.util.{Failure, Success}

case class Project(
    self: String,
    id: String,
    key: String,
    description: Option[String],
    lead: JiraUser,
    issueTypes: List[IssueType]
)

object Project {
  implicit val decoder: Decoder[Project] = deriveDecoder
  implicit val encoder: Encoder[Project] = deriveEncoder
}

case class CreateProjectInput(
    key: String,
    name: String,
    projectTypeKey: ProjectTypeKey,
    projectTemplateKey: String,
    assigneeType: AssigneeType,
    lead: LeadInput,
    description: Option[String] = None,
    url: Option[String] = None,
    avatarId: Option[Int] = None,
    issueSecurityScheme: Option[Int] = None,
    permissionScheme: Option[Int] = None,
    notificationScheme: Option[Int] = None,
    categoryId: Option[Int] = None
)

object CreateProjectInput {
  implicit val encoder: Encoder[CreateProjectInput] = new Encoder[CreateProjectInput] {
    override def apply(in: CreateProjectInput): Json = {
      val leadJson = in.lead match {
        case LeadName(v) => Json.obj("lead" -> Json.fromString(v))
        case LeadAccountId(accId) => Json.obj("leadAccountId" -> Json.fromString(accId))
      }

      val encoded = Json.obj(
        "key" -> Json.fromString(in.key),
        "name" -> Json.fromString(in.name),
        "projectTypeKey" -> in.projectTypeKey.asJson,
        "projectTemplateKey" -> Json.fromString(in.projectTemplateKey),
        "assigneeType" -> in.assigneeType.asJson
      )

      val maybeNulls = Map(
        "description" -> in.description.asJson,
        "url" -> in.url.asJson,
        "avatarId" -> in.avatarId.asJson,
        "issueSecurityScheme" -> in.issueSecurityScheme.asJson,
        "permissionScheme" -> in.permissionScheme.asJson,
        "notificationScheme" -> in.notificationScheme.asJson,
        "categoryId" -> in.categoryId.asJson
      ).filter(!_._2.isNull).asJson

      leadJson.deepMerge(encoded).deepMerge(maybeNulls)
    }
  }
}

sealed trait ProjectTypeKey

object ProjectTypeKey {
  case object Software extends ProjectTypeKey
  case object ServiceDesk extends ProjectTypeKey
  case object Business extends ProjectTypeKey

  implicit val encoder: Encoder[ProjectTypeKey] = new Encoder[ProjectTypeKey] {
    override def apply(p: ProjectTypeKey) = p match {
      case Software => Json.fromString("software")
      case ServiceDesk => Json.fromString("service_desk")
      case Business => Json.fromString("business")
    }
  }

  implicit val decoder: Decoder[ProjectTypeKey] =
    Decoder.decodeString.flatMap { str =>
      Decoder.instanceTry { _ =>
        str match {
          case s if s == "software" => Success(Software)
          case s if s == "service_desk" => Success(ServiceDesk)
          case s if s == "business" => Success(Business)
          case _ => Failure(new Exception("Unable to decode project type key"))
        }
      }
    }
}

sealed trait LeadInput

object LeadInput {
  case class LeadName(value: String) extends LeadInput
  case class LeadAccountId(value: String) extends LeadInput
}

sealed trait AssigneeType

object AssigneeType {
  case object ProjectLead extends AssigneeType
  case object Unassigned extends AssigneeType

  implicit val encoder: Encoder[AssigneeType] = new Encoder[AssigneeType] {
    override def apply(a: AssigneeType) = a match {
      case ProjectLead => Json.fromString("PROJECT_LEAD")
      case Unassigned => Json.fromString("UNASSIGNED")
    }
  }

  implicit val decoder: Decoder[AssigneeType] =
    Decoder.decodeString.flatMap { str =>
      Decoder.instanceTry { _ =>
        str match {
          case s if s == "PROJECT_LEAD" => Success(ProjectLead)
          case s if s == "UNASSIGNED" => Success(Unassigned)
          case _ => Failure(new Exception("Unable to decode project assignee type"))
        }
      }
    }
}
