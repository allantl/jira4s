package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.JiraUser
import com.allantl.jira4s.v2.domain.enums.UserExpand
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp.{SttpBackend, sttp, _}
import cats.syntax.show._

private[jira4s] trait UserClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, _] = backend

  def getUserById(accountId: String, expand: Set[UserExpand] = Set.empty)(
      implicit userCtx: T
  ): R[Either[JiraError, JiraUser]] =
    sttp
      .get(uri"$restEndpoint/user?accountId=$accountId&expand=${expand.map(_.show).mkString(",")}")
      .jiraAuthenticated
      .response(asJson[JiraUser])
      .send()
      .parseResponse

  def getCurrentUser(
      expand: Set[UserExpand] = Set.empty
  )(implicit userCtx: T): R[Either[JiraError, JiraUser]] =
    sttp
      .get(uri"$restEndpoint/myself?expand=${expand.map(_.show).mkString(",")}")
      .jiraAuthenticated
      .followRedirects(true)
      .response(asJson[JiraUser])
      .send()
      .parseResponse
}
