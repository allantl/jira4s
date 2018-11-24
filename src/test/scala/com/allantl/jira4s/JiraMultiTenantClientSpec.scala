package com.allantl.jira4s

import com.allantl.jira4s.auth.AcJwtConfig
import com.allantl.jira4s.v2.JiraMultiTenantClient
import com.softwaremill.sttp.HttpURLConnectionBackend
import org.specs2.mutable.Specification

class JiraMultiTenantClientSpec extends Specification {

  "JiraMultiTenantClient" should {
    "initialize with atlassian connect config" in {
      implicit val sttpBackend = HttpURLConnectionBackend()

      val acConfig = AcJwtConfig("addOnKey", 5L)
      val client = JiraMultiTenantClient.apply(acConfig)

      client must not(throwA[Exception])
    }
  }

}
