package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.Issue
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._

private[jira4s] trait IssueClient[R[_], T <: AuthContext] extends HasClient[R] {

  implicit val be: SttpBackend[R, _] = backend

  def getIssue(issueId: String)(implicit userCtx: T): R[Either[JiraError, Issue]] =
    sttp
      .get(uri"$restEndpoint/issue/$issueId")
      .response(asJson[Issue])
      .jiraAuthenticated
      .send()
      .parseResponse
}
