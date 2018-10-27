package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.allantl.jira4s.v2.domain.{CreateProjectInput, Project, ProjectRef}
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp.{SttpBackend, sttp, _}
import io.circe.syntax._

private[jira4s] trait ProjectClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, Nothing] = backend

  def getProject(projectId: String)(implicit userCtx: T): R[Either[JiraError, Project]] =
    sttp
      .get(uri"$restEndpoint/project/$projectId")
      .jiraAuthenticated
      .response(asJson[Project])
      .send()
      .parseResponse

  def createProject(projectInput: CreateProjectInput)(implicit userCtx: T): R[Either[JiraError, ProjectRef]] =
    sttp
      .post(uri"$restEndpoint/project")
      .body(projectInput.asJson.noSpaces)
      .jiraAuthenticated
      .response(asJson[ProjectRef])
      .send()
      .parseResponse

  def deleteProject(projectId: String)(implicit userCtx: T): R[Either[JiraError, Unit]] =
    sttp
      .delete(uri"$restEndpoint/project/$projectId")
      .jiraAuthenticated
      .send()
      .parseResponse_

}
