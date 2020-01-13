package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.Converter.{toUrlParameters, deleteAdjustEstimateToUrlParameters => deleteConverter, postAdjustEstimateToUrlParameters => postConverter, putAdjustEstimateToUrlParameters => putConverter}
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.allantl.jira4s.v2.domain.{AdjustEstimate, WorkLog, WorkLogCreateResponse, WorkLogPayLoad}
import com.softwaremill.sttp.circe.{asJson, _}
import com.softwaremill.sttp.{SttpBackend, sttp, _}
import io.circe.syntax._

private[jira4s] trait WorkLogClient[R[_], T <: AuthContext] extends HasClient[R] {

  private implicit val be: SttpBackend[R, Nothing] = backend

  def getIssueWorkLog(
                       issueId: String
                     )(
                       implicit userCtx: T
                     ): R[Either[JiraError, List[WorkLog]]] =
    sttp
      .get(uri"$restEndpoint/issue/$issueId/worklog")
      .jiraAuthenticated
      .response(asJson[List[WorkLog]])
      .send()
      .parseResponse

  def getWorkLog(
                  issueId: String,
                  workLogId: String
                )(
                  implicit userCtx: T
                ): R[Either[JiraError, WorkLog]] =
    sttp
      .get(uri"$restEndpoint/issue/$issueId/worklog/$workLogId")
      .jiraAuthenticated
      .response(asJson[WorkLog])
      .send()
      .parseResponse

  def createWorkLog(
                     issueId: String,
                     workLogPayLoad: WorkLogPayLoad,
                     adjustEstimate: AdjustEstimate
                   )(
                     implicit userCtx: T
                   ): R[Either[JiraError, WorkLogCreateResponse]] =
    sttp
      .post(uri"$restEndpoint/issue/$issueId/worklog?${toUrlParameters(adjustEstimate)(postConverter)}")
      .body(workLogPayLoad.asJson)
      .jiraAuthenticated
      .response(asJson[WorkLogCreateResponse])
      .send()
      .parseResponse

  def updateWorkLog(
                     issueId: String,
                     workLogId: String,
                     workLogPayLoad: WorkLogPayLoad,
                     adjustEstimate: AdjustEstimate
                   )(
                     implicit userCtx: T
                   ): R[Either[JiraError, Unit]] =
    sttp
      .put(uri"$restEndpoint/issue/$issueId/worklog/$workLogId?${toUrlParameters(adjustEstimate)(putConverter)}")
      .body(workLogPayLoad.asJson)
      .jiraAuthenticated
      .send()
      .parseResponse_

  def deleteWorkLog(
                     issueId: String,
                     workLogId: String,
                     adjustEstimate: AdjustEstimate
                   )(
                     implicit userCtx: T
                   ): R[Either[JiraError, Unit]] =
    sttp
      .delete(uri"$restEndpoint/issue/$issueId/worklog/$workLogId?${toUrlParameters(adjustEstimate)(deleteConverter)}")
      .jiraAuthenticated
      .send()
      .parseResponse_
}
