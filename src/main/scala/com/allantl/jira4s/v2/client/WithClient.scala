package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth.AuthConfig

private[jira4s] trait WithClient {
  protected def restClient: String
  protected implicit val authConfig: AuthConfig
}
