package com.allantl.jira4s.v2.client

import cats.syntax.show._
import com.allantl.jira4s.auth.AuthContext
import com.allantl.jira4s.v2.domain.SearchResults
import com.allantl.jira4s.v2.domain.enums.{SearchExpand, ValidateQuery}
import com.allantl.jira4s.v2.domain.errors.JiraError
import com.softwaremill.sttp.circe.asJson
import com.softwaremill.sttp.{SttpBackend, sttp, _}

private[jira4s] trait SearchClient[R[_], T <: AuthContext] extends HasClient[R] {

  implicit val be: SttpBackend[R, _] = backend

  def search(
      jql: String,
      startAt: Int = 0,
      maxResults: Int = 50,
      validateQuery: ValidateQuery = ValidateQuery.Strict,
      fields: Option[Set[String]] = None,
      expand: Option[Set[SearchExpand]] = None,
      properties: Option[Set[String]] = None,
      fieldsByKeys: Boolean = false
  )(implicit userCtx: T): R[Either[JiraError, SearchResults]] = {
    val params = Map(
      "jql" -> Some(jql),
      "startAt" -> Some(startAt.toString),
      "maxResults" -> Some(maxResults.toString),
      "validateQuery" -> Some(validateQuery.show),
      "fields" -> fields.map(_.mkString(",")),
      "expand" -> expand.map(_.map(_.show)).map(_.mkString(",")),
      "properties" -> properties.map(_.mkString(",")),
      "fieldsByKeys" -> fieldsByKeys.toString
    )
    sttp
      .get(uri"$restEndpoint/search?$params")
      .jiraAuthenticated
      .response(asJson[SearchResults])
      .send()
      .parseResponse
  }

}
