package com.allantl.jira4s.auth.jwt

import java.net.URI
import java.time.Duration
import java.time.temporal.ChronoUnit

import com.allantl.jira4s.auth.{AtlassianConnectConfig, AuthContext}
import com.allantl.jira4s.auth.jwt.domain.CanonicalURIHttpRequest
import com.allantl.jira4s.auth.jwt.errors._
import io.toolsplus.atlassian.jwt.{HttpRequestCanonicalizer, JwtBuilder}

import scala.util.Try

private[jira4s] object JwtGenerator {

  def generateToken(httpMethod: String, uri: String)(
      implicit acContext: AuthContext,
      acConfig: AtlassianConnectConfig
  ): Either[JwtGeneratorError, String] =
    for {
      uri <- toJavaUri(uri)
      hostUri <- toJavaUri(acContext.instanceUrl)
      _ <- isAbsoluteUri(uri)
      _ <- isRequestToHost(uri, hostUri)
      token <- createToken(httpMethod, uri)
    } yield token

  private def createToken(httpMethod: String, uri: URI)(
      implicit acContext: AuthContext,
      acConfig: AtlassianConnectConfig
  ): Either[JwtGeneratorError, String] = {
    val canonicalHttpRequest = CanonicalURIHttpRequest(httpMethod, uri)
    val queryHash = HttpRequestCanonicalizer.computeCanonicalRequestHash(canonicalHttpRequest)
    val expireAfter = Duration.of(acConfig.jwtExpirationInSeconds, ChronoUnit.SECONDS)

    new JwtBuilder(expireAfter)
      .withIssuer(acConfig.addOnKey)
      .withQueryHash(queryHash)
      .build(acContext.accessToken)
      .left
      .map(_ => InvalidSigningError)
  }

  private def toJavaUri(str: String): Either[JwtGeneratorError, URI] =
    Try(new URI(str)).toEither.left.map(_ => InvalidUriError)

  private def isAbsoluteUri(uri: URI): Either[JwtGeneratorError, URI] =
    if (uri.isAbsolute) Right(uri) else Left(RelativeUriError)

  private def isRequestToHost(uri: URI, hostUri: URI): Either[JwtGeneratorError, URI] = {
    val isReqToHost = !hostUri.relativize(uri).isAbsolute
    if (isReqToHost) Right(uri) else Left(BaseUrlMismatchError)
  }

}
