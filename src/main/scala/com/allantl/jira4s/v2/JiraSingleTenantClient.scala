package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, NoCtx}
import com.allantl.jira4s.v2.client.IssueClient
import com.softwaremill.sttp.{HttpURLConnectionBackend, SttpBackend}

trait JiraSingleTenantClient[R[_]] extends IssueClient[R, NoCtx]

object JiraSingleTenantClient {
  def apply[R[_], S](apiToken: ApiToken)(implicit be: SttpBackend[R, S]): JiraSingleTenantClient[R] =
    new JiraSingleTenantClient[R] {
      override protected implicit val backend = be
      override protected implicit val authConfig = apiToken
    }
}

object App {
  implicit val backend = HttpURLConnectionBackend()
  JiraSingleTenantClient(ApiToken("asd", "asd", "asd")).getIssue("")
}
