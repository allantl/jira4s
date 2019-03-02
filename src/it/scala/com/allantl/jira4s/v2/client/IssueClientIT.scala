package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest
import com.allantl.jira4s.v2.context.ProjectRefContext
import com.allantl.jira4s.v2.domain.{IssueField, _}

class IssueClientIT extends IntegrationTest with ProjectRefContext {

  "IssueClient" should {
    "create issue" in projectRef() { p: ProjectRef =>
      val payload = IssuePayload(
        IssueField("summary", "IT"),
        IssueField("project", "id" -> p.id.toString),
        IssueField("issuetype", "id" -> "10002")
      )
      val issue = client.createIssue(payload)
      issue must beRight
    }
  }
}
