package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.Issue
import com.allantl.jira4s.v2.domain.errors.{GenericError, JiraError, JiraResponseError}
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.parser._

private[jira4s] trait IssueClient[R[_], T <: AuthContext] extends HasClient[R] {

  implicit val be = backend

  def getIssue(issueId: String)(implicit userCtx: T): R[Either[JiraError, Issue]] =
    rm.map(
      sttp
        .get(uri"$restEndpoint/issue/$issueId")
        .response(asJson[Issue])
        .jiraAuthenticated
        .send()
    ) {
      case Response(Right(result), _, _, _, _) =>
        result.fold(
          _ => Left(GenericError("Unable to parse JSON")),
          res => Right(res)
        )

      case Response(Left(errMsg), _, _, _, _) =>
        parse(new String(errMsg)).toOption
          .flatMap(_.as[JiraResponseError].toOption)
          .fold(Left(GenericError("Unknown Error")))(err =>
            Left(GenericError(err.errorMessages.headOption.getOrElse("Unknown Error"))))
    }
}
