package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class WorkLogPayLoad(comment: Option[String], started: String, timeSpentSeconds: Int)

object WorkLogPayLoad {
  implicit val issueDecoder: Decoder[WorkLogPayLoad] = deriveDecoder
  implicit val issueEncoder: Encoder[WorkLogPayLoad] = deriveEncoder
}

case class WorkLogCreateResponse(id: String, self: String)

object WorkLogCreateResponse {
  implicit val issueDecoder: Decoder[WorkLogCreateResponse] = deriveDecoder
  implicit val issueEncoder: Encoder[WorkLogCreateResponse] = deriveEncoder
}

sealed trait AdjustEstimate

case class NewEstimate(newEstimate: String) extends AdjustEstimate

case object LeaveEstimate extends AdjustEstimate

case class ManualEstimate(newEstimate: String) extends AdjustEstimate

case object AutoEstimate extends AdjustEstimate

object Converter {

  def toUrlParameters[A: ToUrlParametersConverter](v: A): String =
    implicitly[ToUrlParametersConverter[A]].convert(v)

  sealed trait ToUrlParametersConverter[A] {
    def convert(estimate: A): String
  }

  implicit val postAdjustEstimateToUrlParameters = new ToUrlParametersConverter[AdjustEstimate] {
    override def convert(estimate: AdjustEstimate): String =
      estimate match {
        case LeaveEstimate => "adjustEstimate=leave"
        case AutoEstimate => "adjustEstimate=auto"
        case ManualEstimate(newEstimate) => s"adjustEstimate=manual&reduceBy=$newEstimate"
        case NewEstimate(newEstimate) => s"adjustEstimate=new&newEstimate=$newEstimate"
      }
  }

  implicit val putAdjustEstimateToUrlParameters = new ToUrlParametersConverter[AdjustEstimate] {
    override def convert(estimate: AdjustEstimate): String =
      estimate match {
        case LeaveEstimate => "adjustEstimate=leave"
        case AutoEstimate | _: ManualEstimate => "adjustEstimate=auto"
        case NewEstimate(newEstimate) => s"adjustEstimate=new&newEstimate=$newEstimate"
      }
  }

  implicit val deleteAdjustEstimateToUrlParameters = new ToUrlParametersConverter[AdjustEstimate] {
    override def convert(estimate: AdjustEstimate): String =
      estimate match {
        case LeaveEstimate => "adjustEstimate=leave"
        case AutoEstimate => "adjustEstimate=auto"
        case ManualEstimate(newEstimate) => s"adjustEstimate=manual&increaseBy=$newEstimate"
        case NewEstimate(newEstimate) => s"adjustEstimate=new&newEstimate=$newEstimate"
      }
  }
}
