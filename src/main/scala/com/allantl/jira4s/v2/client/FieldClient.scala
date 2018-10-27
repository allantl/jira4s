package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.Field
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp.{SttpBackend, sttp, _}

private[jira4s] trait FieldClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, Nothing] = backend

  def getFields()(implicit userCtx: T): R[Either[JiraError, List[Field]]] =
    sttp
      .get(uri"$restEndpoint/field")
      .jiraAuthenticated
      .response(asJson[List[Field]])
      .send()
      .parseResponse
}
