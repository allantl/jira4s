package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, BasicAuthentication, NoCtx}
import com.allantl.jira4s.v2.client.IssueClient
import com.softwaremill.sttp.SttpBackend

sealed trait JiraSingleTenantClient[R[_]] extends IssueClient[R, NoCtx]

object JiraSingleTenantClient {
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
      override protected lazy val backend  = sttpBackend
      override protected lazy val authConfig = basicAuth
    }
}
