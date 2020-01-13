package com.allantl.jira4s.v2.client

import cats.implicits._
import com.allantl.jira4s.mocks.MockJiraSingleTenantClient
import com.allantl.jira4s.utils._
import com.allantl.jira4s.v2.domain.{LeaveEstimate, ManualEstimate, NewEstimate, WorkLogPayLoad}
import com.softwaremill.sttp.Uri.QueryFragment.Value
import com.softwaremill.sttp.testing.SttpBackendStub
import org.specs2.mutable.Specification

class WorkLogClientSpec extends Specification {
  private val backend = SttpBackendStub.synchronous
  private val issueId = "10000"
  private val workLogId = "773125"

  "WorkLogClient" should {
    "get work log" in {
      implicit val testingBackend = backend
        .whenRequestMatches(_.uri.path.endsWith(List("worklog", workLogId)))
        .thenRespond(loadJson("worklog.json"))

      val client = MockJiraSingleTenantClient()
      client.getWorkLog(issueId, workLogId) must beRight
    }

    "create work log" in {
      implicit val testingBackend = backend
        .whenRequestMatches { r =>
          r.method.m == "POST" &&
            r.uri.path.endsWith(List("worklog")) &&
            r.uri.queryFragments == Seq(Value("adjustEstimate=leave"))
        }
        .thenRespondWithCode(code = 201, msg = loadJson("workLog-ref.json"))

      val client = MockJiraSingleTenantClient()
      val res = client.createWorkLog(
        issueId,
        WorkLogPayLoad("Done".some, "2019-12-30T12:22:00.000+0000", 3600),
        LeaveEstimate
      )

      res must beRight
    }

    "update work log" in {
      implicit val testingBackend = backend
        .whenRequestMatches { r =>
          r.method.m == "PUT" &&
            r.uri.path.endsWith(List("worklog", workLogId)) &&
            r.uri.queryFragments == Seq(Value("adjustEstimate=auto"))
        }
        .thenRespondWithCode(code = 200)

      val client = MockJiraSingleTenantClient()
      val res = client.updateWorkLog(
        issueId,
        workLogId,
        WorkLogPayLoad("Done".some, "2019-12-30T12:22:00.000+0000", 3600),
        ManualEstimate("2d")
      )

      res must beRight
    }

    "delete work log" in {
      implicit val testingBackend = backend
        .whenRequestMatches { r =>
          r.method.m == "DELETE" &&
            r.uri.path.endsWith(List("worklog", workLogId)) &&
            r.uri.queryFragments == Seq(Value("adjustEstimate=new&newEstimate=2d"))
        }
        .thenRespondWithCode(code = 204)

      val client = MockJiraSingleTenantClient()
      val res = client.deleteWorkLog(
        issueId,
        workLogId,
        NewEstimate("2d")
      )

      res must beRight
    }

  }
}
