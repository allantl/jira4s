package com.allantl.jira4s.v2.context

import com.allantl.jira4s.v2.Client
import com.allantl.jira4s.v2.Utils.randomStr
import com.allantl.jira4s.v2.domain.LeadInput.LeadName
import com.allantl.jira4s.v2.domain.{AssigneeType, CreateProjectInput, ProjectRef, ProjectTypeKey}
import org.specs2.execute.{AsResult, Failure, FailureException}
import org.specs2.specification.Fixture

trait ProjectRefContext extends Client {

  def projectRef() = new Fixture[ProjectRef] {
    def apply[R: AsResult](f: ProjectRef => R) = {
      val projectRef = createProject()
      try AsResult(f(projectRef))
      finally cleanProject(projectRef)
    }
  }

  private def createProject(): ProjectRef = {
    val pKey = randomStr(4).toUpperCase
    val pName = randomStr(5).toUpperCase
    val created = client.createProject(
      CreateProjectInput(
        pKey,
        pName,
        ProjectTypeKey.Business,
        "com.atlassian.jira-core-project-templates:jira-core-simplified-project-management",
        AssigneeType.Unassigned,
        LeadName("admin")
      )
    )

    created match {
      case Left(err) =>
        throw FailureException(Failure(s"[jira4s] Error creating project due to ${err.errMsg}"))
      case Right(pRef) => pRef
    }
  }

  private def cleanProject(ref: ProjectRef): Unit =
    client
      .deleteProject(ref.id.toString)
      .fold(
        err => println(s"[jira4s] Error removing project ${ref.key} due to ${err.errMsg}"),
        _ => ()
      )
}
