package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, AtlassianConnectConfig, AuthConfig, BasicAuthentication}
import com.typesafe.config.ConfigFactory

import scala.util.Try

private[jira4s] object Config {

  private val config = ConfigFactory.load

  private def getConfig(path: String): String = {
    config.getString(s"jira4s.$path")
  }

  private def loadBasicAuth(): BasicAuthentication = {
    val jiraUrl = getConfig("jira-url")
    val username = getConfig("username")
    val password = getConfig("password")

    BasicAuthentication(jiraUrl, username, password)
  }

  private def loadApiToken(): ApiToken = {
    val jiraUrl = getConfig("jira-url")
    val email = getConfig("email")
    val apiToken = getConfig("api-token")

    ApiToken(jiraUrl, email, apiToken)
  }

  private def loadAcConfig(): AtlassianConnectConfig = {
    val addOnKey = getConfig("add-on-key")
    val jwtExpiration = getConfig("jwt-expiration-in-seconds").toLong

    AtlassianConnectConfig(addOnKey, jwtExpiration)
  }

  def loadSingleTenantConfig(): AuthConfig = {
    val conf = Try(loadApiToken()).recoverWith {
      case _ => Try(loadBasicAuth())
    }

    if (conf.isFailure) {
      throw new RuntimeException(s"[jira4s] Configuration for single tenant client not specified")
    } else {
      conf.get
    }
  }

  def loadMultiTenantConfig(): AuthConfig = {
    val conf = Try(loadAcConfig())

    if (conf.isFailure) {
      throw new RuntimeException(s"[jira4s] Configuration for multi tenant client invalid/not specified")
    } else {
      conf.get
    }
  }

}
