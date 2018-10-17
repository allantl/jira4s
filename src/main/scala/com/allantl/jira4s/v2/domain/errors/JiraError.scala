package com.allantl.jira4s.v2.domain.errors

abstract class JiraError(msg: String) extends Exception(msg)

case class GenericError(msg: String) extends JiraError(msg)
