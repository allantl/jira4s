package com.allantl.jira4s.auth

trait AuthContext {
  def instanceUrl: String
  def accessToken: String
}
