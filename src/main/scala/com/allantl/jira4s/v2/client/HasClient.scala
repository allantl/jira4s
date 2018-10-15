package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth._
import com.softwaremill.sttp.SttpBackend

private[jira4s] trait HasAuthConfig {

  protected val authConfig: AuthConfig

  protected def instanceUrl[T <: AuthContext](implicit userCtx: T): String =
    authConfig match {
      case BasicCredentials(jiraUrl, _, _) => jiraUrl
      case ApiToken(jiraUrl, _, _) => jiraUrl
      case _ => userCtx.instanceUrl
    }
}

private[jira4s] trait HasBackend[R[_]] {
  protected implicit val backend: SttpBackend[R, _]
  protected val rm = backend.responseMonad
}

private[jira4s] trait HasClient[R[_]] extends HasAuthConfig with HasBackend[R]
