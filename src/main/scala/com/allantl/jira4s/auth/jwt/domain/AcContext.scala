package com.allantl.jira4s.auth.jwt.domain

trait AcContext {
  def instanceUrl: String
  def accessToken: String
}
