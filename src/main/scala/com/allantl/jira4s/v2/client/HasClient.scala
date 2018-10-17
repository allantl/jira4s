package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth._
import com.allantl.jira4s.v2
import com.softwaremill.sttp.{Request, SttpBackend}

private[jira4s] trait HasAuthConfig {

  protected val authConfig: AuthConfig

  protected def restEndpoint[T <: AuthContext](implicit userCtx: T): String =
    s"$instanceUrl/${v2.apiUrl}"

  protected def instanceUrl[T <: AuthContext](implicit userCtx: T): String =
    authConfig match {
      case BasicCredentials(jiraUrl, _, _) => jiraUrl
      case ApiToken(jiraUrl, _, _) => jiraUrl
      case _ => userCtx.instanceUrl
    }
}

private[jira4s] trait HasBackend[R[_]] {
  protected def backend: SttpBackend[R, _]
  protected lazy val rm = backend.responseMonad
}

private[jira4s] trait HasClient[R[_]] extends HasAuthConfig with HasBackend[R] {
  implicit class RequestOps[T, +S](req: Request[T, S]) {
    def jiraAuthenticated[Ctx <: AuthContext](implicit userCtx: Ctx): Request[T, S] =
      authConfig match {
        case BasicCredentials(_, username, password) =>
          req.auth.basic(username, password)
        case ApiToken(_, email, apiToken) =>
          req.auth.basic(email, apiToken)
        case _ =>
          req.auth.bearer(userCtx.accessToken)
      }
  }
}
