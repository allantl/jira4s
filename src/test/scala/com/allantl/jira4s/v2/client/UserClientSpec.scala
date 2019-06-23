package com.allantl.jira4s.v2.client

import com.allantl.jira4s.mocks.MockJiraSingleTenantClient
import com.allantl.jira4s.utils._
import com.softwaremill.sttp.testing.SttpBackendStub
import org.specs2.mutable.Specification

class UserClientSpec extends Specification {

  private val backend = SttpBackendStub.synchronous

  "UserClient" should {
    "get myself" in {
      implicit val testingBackend = backend
        .whenRequestMatches(_.uri.path.endsWith(List("myself")))
        .thenRespond(loadJson("myself.json"))

      val client = MockJiraSingleTenantClient()
      client.getCurrentUser() must beRight
    }
  }
}
