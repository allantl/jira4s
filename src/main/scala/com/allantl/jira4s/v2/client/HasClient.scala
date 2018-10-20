package com.allantl.jira4s.v2.client

import com.allantl.jira4s.auth._
import com.allantl.jira4s.v2
import com.allantl.jira4s.v2.domain.errors._
import com.softwaremill.sttp.{DeserializationError, Request, Response, StatusCodes, SttpBackend}
import io.circe.parser._

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
  protected lazy val rm = backend.responseMonad

  protected def backend: SttpBackend[R, _]
}

private[jira4s] trait HasClient[R[_]] extends HasAuthConfig with HasBackend[R] {

  private def parseError(errByte: Array[Byte], status: Int): JiraError = {
    val jiraResponseError = parse(new String(errByte)).toOption
      .flatMap(_.as[JiraResponseError].toOption)

    val errMsg = jiraResponseError
      .flatMap(_.errorMessages.headOption)
      .orElse(jiraResponseError.flatMap(_.errors.headOption.map(_._2)))

    val err: JiraError = status match {
      case StatusCodes.Forbidden =>
        errMsg.fold(AccessDeniedError("Access forbidden!"))(AccessDeniedError)

      case StatusCodes.Unauthorized =>
        errMsg.fold(UnauthorizedError("Invalid JIRA credentials or access forbidden!"))(
          UnauthorizedError)

      case StatusCodes.NotFound =>
        errMsg.fold(ResourceNotFound("Resource not found!"))(ResourceNotFound)

      case _ =>
        errMsg.fold(GenericError("Unknown Error!"))(GenericError)
    }
    err
  }

  implicit class RequestOps[T](req: Request[T, Nothing]) {
    def jiraAuthenticated[Ctx <: AuthContext](implicit userCtx: Ctx): Request[T, Nothing] =
      authConfig match {
        case BasicCredentials(_, username, password) =>
          req.auth.basic(username, password)
        case ApiToken(_, email, apiToken) =>
          req.auth.basic(email, apiToken)
        case _ =>
          req.auth.bearer(userCtx.accessToken)
      }
  }

  implicit class SttpResponseOps(r: R[Response[String]]) {
    def parseResponse: R[Either[JiraError, Unit]] = rm.map(r) {
      case Response(Right(_), _, _, _, _) =>
        Right(())

      case Response(Left(errByte), statusCode, _, _, _) =>
        Left(parseError(errByte, statusCode))
    }
  }

  implicit class ResponseOps[T](r: R[Response[Either[DeserializationError[io.circe.Error], T]]]) {
    def parseResponse: R[Either[JiraError, T]] = rm.map(r) {
      case Response(Right(result), _, _, _, _) =>
        result.fold(
          _ => Left(JsonDeserializationError),
          res => Right(res)
        )

      case Response(Left(errByte), statusCode, _, _, _) =>
        Left(parseError(errByte, statusCode))
    }
  }
}
