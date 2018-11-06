package com.allantl.jira4s.v2

import com.allantl.jira4s.auth.{ApiToken, AtlassianConnectConfig, AuthConfig, BasicAuthentication}
import com.typesafe.config.ConfigFactory

import scala.util.Try
import ConfigImplicits._

private[jira4s] object Config {

  private implicit val config = ConfigFactory.load

  private val urlEnv = "JIRA_URL"
  private val emailEnv = "EMAIL"
  private val apiTokenEnv = "API_TOKEN"
  private val passwordEnv = "PASSWORD"

  private def loadApiToken(): Option[ApiToken] =
    ("jira-url", "email", "api-token")
      .loadConfig((x, y, z) => Some(ApiToken(x, y, z)))
      .orElse(
        (urlEnv, emailEnv, apiTokenEnv)
          .loadEnv((x, y, z) => Some(ApiToken(x, y, z)))
      )

  private def loadBasicAuth(): Option[BasicAuthentication] =
    ("jira-url", "email", "password")
      .loadConfig((x, y, z) => Some(BasicAuthentication(x, y, z)))
      .orElse(
        (urlEnv, emailEnv, passwordEnv)
          .loadEnv((x, y, z) => Some(BasicAuthentication(x, y, z)))
      )

  private def loadAcConfig(): Option[AtlassianConnectConfig] = {
    val expStringToLong = (exp: String) => Try(exp.toLong).toOption

    ("add-on-key", "jwt-expiration-in-seconds")
      .loadConfig((addOnKey, jwtExp) =>
        expStringToLong(jwtExp).map(AtlassianConnectConfig(addOnKey, _)))
      .orElse(
        ("ADD_ON_KEY", "JWT_EXPIRATION_IN_SECONDS").loadEnv((addOnKey, jwtExp) =>
          expStringToLong(jwtExp).map(AtlassianConnectConfig(addOnKey, _)))
      )
  }

  def loadSingleTenantConfig(): AuthConfig = {
    val config: Option[AuthConfig] = loadApiToken().orElse(loadBasicAuth())
    config.fold(
      throw new RuntimeException(s"[jira4s] Configuration for single tenant client not specified")
    )(identity)
  }

  def loadMultiTenantConfig(): AuthConfig = {
    val config: Option[AuthConfig] = loadAcConfig()
    config.fold(
      throw new RuntimeException(s"[jira4s] Configuration for multi tenant client invalid/not specified")
    )(identity)
  }

}
