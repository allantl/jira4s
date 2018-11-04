package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{AtlassianConnectConfig, AuthContext}
import com.allantl.jira4s.v2.client.{FieldClient, IssueClient, ProjectClient, SearchClient}
import com.softwaremill.sttp.SttpBackend

sealed trait JiraMultiTenantClient[R[_]]
    extends IssueClient[R, AuthContext]
    with SearchClient[R, AuthContext]
    with ProjectClient[R, AuthContext]
    with FieldClient[R, AuthContext]

object JiraMultiTenantClient {

  def apply[R[_], S]()(
    implicit sttpBackend: SttpBackend[R, S]
  ): JiraMultiTenantClient[R] = {
    val conf = Config.loadMultiTenantConfig()
    new JiraMultiTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = conf
    }
  }

  def apply[R[_], S](acConfig: AtlassianConnectConfig)(
      implicit sttpBackend: SttpBackend[R, S]
  ): JiraMultiTenantClient[R] =
    new JiraMultiTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = acConfig
    }
}
