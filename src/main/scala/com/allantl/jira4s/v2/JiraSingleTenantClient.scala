package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, BasicAuthentication, NoCtx}
import com.allantl.jira4s.v2.client._
import com.softwaremill.sttp.SttpBackend

sealed trait JiraSingleTenantClient[R[_]]
  extends IssueClient[R, NoCtx]
    with WorkLogClient[R, NoCtx]
    with SearchClient[R, NoCtx]
    with ProjectClient[R, NoCtx]
    with FieldClient[R, NoCtx]
    with UserClient[R, NoCtx]
    with PriorityClient[R, NoCtx]

object JiraSingleTenantClient {

  def apply[R[_], S]()(
    implicit sttpBackend: SttpBackend[R, S]
  ): JiraSingleTenantClient[R] = {
    val conf = Config.loadSingleTenantConfig()
    new JiraSingleTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = conf
    }
  }

  def apply[R[_], S](apiToken: ApiToken)(
    implicit sttpBackend: SttpBackend[R, S]
  ): JiraSingleTenantClient[R] =
    new JiraSingleTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = apiToken
    }

  def apply[R[_], S](basicAuth: BasicAuthentication)(
    implicit sttpBackend: SttpBackend[R, S]
  ): JiraSingleTenantClient[R] =
    new JiraSingleTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = basicAuth
    }
}
