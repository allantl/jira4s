package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.domain.Issue

private[jira4s] trait IssueClient[T] extends WithClient {

  def getIssue()(implicit userCtx: T): Issue = ???

}
