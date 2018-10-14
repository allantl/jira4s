package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, NoCtx}
import com.allantl.jira4s.v2.client.IssueClient

trait JiraSingleTenantClient extends IssueClient[NoCtx]

object JiraSingleTenantClient {
  def apply(apiToken: ApiToken) =
    new JiraSingleTenantClient {
      override protected def restClient = null
      override protected implicit val authConfig = null
    }
}

object App {

  JiraSingleTenantClient(ApiToken("asd", "asd", "asd")).getIssue()

}
