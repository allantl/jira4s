package com.allantl.jira4s.v2.client

import com.allantl.jira4s.mocks.MockJiraSingleTenantClient
import com.allantl.jira4s.utils._
import com.allantl.jira4s.v2.domain.{IssueField, IssuePayload}
import com.softwaremill.sttp.testing.SttpBackendStub
import io.circe.Json
import org.specs2.mutable.Specification

class IssueClientSpec extends Specification {

  private val backend = SttpBackendStub.synchronous
  private val issueId = "10000"

  "IssueClient" should {
    "get issue" in {
      implicit val testingBackend = backend
        .whenRequestMatches(_.uri.path.endsWith(List("issue", issueId)))
        .thenRespond(loadJson("issue.json"))

      val client = MockJiraSingleTenantClient()
      client.getIssue(issueId) must beRight
    }

    "create issue" in {
      implicit val testingBackend = backend
        .whenRequestMatches { r =>
          r.method.m == "POST" &&
            r.uri.path.endsWith(List("issue")) &&
            assertStringBody(r.body)(m => {
              val fields =
                (s: String) => m.get("fields").flatMap(_.hcursor.downField(s).as[Json].toOption)
              fields("project").contains(Json.obj("id" -> Json.fromString("10000"))) &&
              fields("summary").contains(Json.fromString("An Issue")) &&
              fields("issuetype").contains(Json.obj("id" -> Json.fromString("10000")))
            })
        }
        .thenRespondWithCode(code = 201, msg = loadJson("issue-ref.json"))

      val client = MockJiraSingleTenantClient()
      val res = client.createIssue(
        IssuePayload(
          IssueField("project", "id" -> "10000"),
          IssueField("summary", "An Issue"),
          IssueField("issuetype", "id" -> "10000")
        )
      )

      res must beRight
    }

    "update issue" in {
      implicit val testingBackend = backend
        .whenRequestMatches { r =>
          r.method.m == "PUT" &&
            r.uri.path.endsWith(List("issue", issueId)) &&
            assertStringBody(r.body)(m => {
              val fields =
                (s: String) => m.get("fields").flatMap(_.hcursor.downField(s).as[Json].toOption)
              fields("summary").contains(Json.fromString("Updated"))
            })
        }
        .thenRespondWithCode(code = 204)

      val client = MockJiraSingleTenantClient()
      val res = client.updateIssue(
        issueId,
        IssuePayload(
          IssueField("summary", "Updated"),
        )
      )

      res must beRight
    }

    "get issue property" in {
      implicit val testingBackend = backend
        .whenRequestMatches(
          _.uri.path.endsWith(List("issue", issueId, "properties", "com.allantl.jira4s"))
        )
        .thenRespond(loadJson("issue-property.json"))

      val client = MockJiraSingleTenantClient()

      client.getIssueProperty(issueId, "com.allantl.jira4s") must beRight
    }
  }

}
