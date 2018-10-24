package com.allantl.jira4s

import com.allantl.jira4s.auth.{ApiToken, BasicAuthentication}
import com.allantl.jira4s.v2.JiraSingleTenantClient
import com.softwaremill.sttp.HttpURLConnectionBackend
import org.specs2.mutable.Specification

class JiraSingleTenantClientTest extends Specification {

  "JiraSingleTenantClientTest" should {
    "be initialized successfully with api token" in {
      implicit val backend = HttpURLConnectionBackend()

      val apiToken = ApiToken("https://www.jira.atlassian.net", "user@gmail.com", "apiToken")
      val clientWithApiToken = JiraSingleTenantClient(apiToken)

      clientWithApiToken must not(throwA[Exception])
    }

    "be initialized successfully with basic authentication" in {
      implicit val backend = HttpURLConnectionBackend()

      val basicCredentials = BasicAuthentication("https://www.jira.atlassian.net", "user", "pass")
      val clientWithBasicCredentials = JiraSingleTenantClient(basicCredentials)

      clientWithBasicCredentials must not(throwA[Exception])
    }
  }
}
