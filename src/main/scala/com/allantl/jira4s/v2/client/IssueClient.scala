package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.{Issue, IssueCreateResponse, IssuePayload, IssueProperty}
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.syntax._

private[jira4s] trait IssueClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, Nothing] = backend

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

  def updateIssue(
      issueId: String,
      issuePayload: IssuePayload,
      notifyUsers: Boolean = true,
      overrideScreenSecurity: Boolean = false,
      overrideEditableFlag: Boolean = false
  )(implicit userCtx: T): R[Either[JiraError, Unit]] = {
    val params = Map(
      "notifyUsers" -> notifyUsers.toString,
      "overrideScreenSecurity" -> overrideScreenSecurity.toString,
      "overrideEditableFlag" -> overrideEditableFlag.toString
    )
    sttp
      .put(uri"$restEndpoint/issue/$issueId".params(params))
      .body(issuePayload.asJson)
      .jiraAuthenticated
      .send()
      .parseResponse_
  }

  def deleteIssue(issueId: String, deleteSubTasks: Boolean = false)(
      implicit userCtx: T
  ): R[Either[JiraError, Unit]] =
    sttp
      .delete(uri"$restEndpoint/issue/$issueId?deleteSubTasks=$deleteSubTasks")
      .jiraAuthenticated
      .send()
      .parseResponse_

  def getIssueProperty(issueId: String, propertyKey: String)(
      implicit userCtx: T
  ): R[Either[JiraError, IssueProperty]] =
    sttp
      .get(uri"$restEndpoint/issue/$issueId/properties/$propertyKey")
      .jiraAuthenticated
      .response(asJson[IssueProperty])
      .send()
      .parseResponse

  def setIssueProperty(issueId: String, propertyKey: String)(data: String)(
      implicit userCtx: T
  ): R[Either[JiraError, Unit]] =
    sttp
      .put(uri"$restEndpoint/issue/$issueId/properties/$propertyKey")
      .body(data)
      .jiraAuthenticated
      .send()
      .parseResponse_

  def deleteIssueProperty(issueId: String, propertyKey: String)(
    implicit userCtx: T
  ): R[Either[JiraError, Unit]] =
    sttp
      .delete(uri"$restEndpoint/issue/$issueId/properties/$propertyKey")
      .jiraAuthenticated
      .send()
      .parseResponse_
}
