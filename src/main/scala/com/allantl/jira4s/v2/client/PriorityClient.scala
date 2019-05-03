package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.Priority
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp.{SttpBackend, sttp, _}

private[jira4s] trait PriorityClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, Nothing] = backend

  def getPriorities()(implicit userCtx: T): R[Either[JiraError, List[Priority]]] =
    sttp
      .get(uri"$restEndpoint/priority")
      .jiraAuthenticated
      .response(asJson[List[Priority]])
      .send()
      .parseResponse

  def getPriority(id: String)(implicit userCtx: T): R[Either[JiraError, Priority]] =
    sttp
      .get(uri"$restEndpoint/priority/$id")
      .jiraAuthenticated
      .response(asJson[Priority])
      .send()
      .parseResponse
}
