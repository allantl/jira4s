package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{AtlassianConnectConfig, AuthContext}
import com.allantl.jira4s.v2.client.IssueClient
import com.softwaremill.sttp.SttpBackend

sealed trait JiraMultiTenantClient[R[_]] extends IssueClient[R, AuthContext]

object JiraMultiTenantClient {
  def apply[R[_], S](acConfig: AtlassianConnectConfig)(
      implicit sttpBackend: SttpBackend[R, S]
  ): JiraMultiTenantClient[R] =
    new JiraMultiTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = acConfig
    }
}
