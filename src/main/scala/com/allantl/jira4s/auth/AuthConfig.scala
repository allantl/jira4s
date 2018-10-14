package com.allantl.jira4s.auth

sealed trait AuthConfig
case class BasicCredentials(username: String, password: String) extends AuthConfig
case class AtlassianConnectConfig(addOnKey: String, jwtExpirationInSeconds: Long) extends AuthConfig
case class ApiToken(jiraUrl: String, email: String, apiToken: String) extends AuthConfig
case object OAUth extends AuthConfig
