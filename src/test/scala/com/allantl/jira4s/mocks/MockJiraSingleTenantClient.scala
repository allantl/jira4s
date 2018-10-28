package com.allantl.jira4s.mocks

import com.allantl.jira4s.auth.ApiToken
import com.allantl.jira4s.v2.JiraSingleTenantClient
import com.softwaremill.sttp.SttpBackend

object MockJiraSingleTenantClient {

  def apply[R[_], S]()(
      implicit sttpBackend: SttpBackend[R, S]
  ): JiraSingleTenantClient[R] =
    JiraSingleTenantClient(
      ApiToken(
        "https://www.test.atlassian.net",
        "email",
        "apiToken"
      ))
}
