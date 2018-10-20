package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.{Issue, IssueCreateResponse, IssuePayload}
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.syntax._

private[jira4s] trait IssueClient[R[_], T <: AuthContext] extends HasClient[R] {

  implicit val be: SttpBackend[R, _] = backend

  def getIssue(issueId: String)(implicit userCtx: T): R[Either[JiraError, Issue]] =
    sttp
      .get(uri"$restEndpoint/issue/$issueId")
      .jiraAuthenticated
      .response(asJson[Issue])
      .send()
      .parseResponse

  def createIssue(issuePayload: IssuePayload, updateHistory: Boolean = false)(
      implicit userCtx: T
  ): R[Either[JiraError, IssueCreateResponse]] =
    sttp
      .post(uri"$restEndpoint/issue?updateHistory=$updateHistory")
      .body(issuePayload.asJson)
      .jiraAuthenticated
      .response(asJson[IssueCreateResponse])
      .send()
      .parseResponse

  def deleteIssue(issueId: String, deleteSubTasks: Boolean = false)(
      implicit userCtx: T
  ): R[Either[JiraError, Unit]] =
    sttp
      .delete(uri"$restEndpoint/issue/$issueId?deleteSubTasks=$deleteSubTasks")
      .jiraAuthenticated
      .send()
      .parseResponse
}
