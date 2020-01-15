package com.allantl.jira4s.v2.client

import com.allantl.jira4s.mocks.MockJiraSingleTenantClient
import com.allantl.jira4s.v2.domain.errors.ResourceNotFound
import com.softwaremill.sttp.testing.SttpBackendStub
import org.specs2.mutable.Specification

class ClientErrorSpec extends Specification {

  val errorMsg =
    """{"errorMessages":["Issue does not exist or you do not have permission to see it."],"errors":{}}"""

  implicit val testingBackend = SttpBackendStub.synchronous
    .whenRequestMatches(_ => true)
    .thenRespondWithCode(404, errorMsg)

  val client = MockJiraSingleTenantClient()

  "JiraClient" should {
    "parse error from Jira" in {
      val res = client.getIssue("10000")
      res must beLeft.like {
        case e: ResourceNotFound =>
          e.msg == "Issue does not exist or you do not have permission to see it."
      }
    }
  }

}
