package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{AcJwtConfig, AuthContext}
import com.allantl.jira4s.v2.client._
import com.softwaremill.sttp.SttpBackend

sealed trait JiraMultiTenantClient[R[_]]
  extends IssueClient[R, AuthContext]
    with WorkLogClient[R, AuthContext]
    with SearchClient[R, AuthContext]
    with ProjectClient[R, AuthContext]
    with FieldClient[R, AuthContext]
    with UserClient[R, AuthContext]
    with PriorityClient[R, AuthContext]

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

  def apply[R[_], S](acConfig: AcJwtConfig)(
    implicit sttpBackend: SttpBackend[R, S]
  ): JiraMultiTenantClient[R] =
    new JiraMultiTenantClient[R] {
      override protected lazy val backend = sttpBackend
      override protected lazy val authConfig = acConfig
    }
}
