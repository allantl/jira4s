package com.allantl.jira4s.auth.jwt

import com.allantl.jira4s.auth.AcJwtConfig
import com.allantl.jira4s.auth.jwt.errors.{
  BaseUrlMismatchError,
  InvalidSecretKey,
  JwtGeneratorError,
  RelativeUriError
}
import com.allantl.jira4s.mocks.MockAuthContext
import io.toolsplus.atlassian.jwt.JwtParser
import org.specs2.mutable.Specification

class JwtGeneratorSpec extends Specification {

  val atlassianUrl = "http://www.allantl.atlassian.net"
  implicit val acConfig = AcJwtConfig("com.allantl.http4s", 5L)

  "JWTGenerator" should {
    "fail if URI does not match with host" in {
      implicit val ctx = MockAuthContext(atlassianUrl)
      val result = JwtGenerator.generateToken("GET", "https://www.google.com")

      result must beLeft(BaseUrlMismatchError: JwtGeneratorError)
    }

    "fail if URI is not absolute" in {
      implicit val ctx = MockAuthContext(atlassianUrl)
      val result = JwtGenerator.generateToken("GET", "atlassian.com")

      result must beLeft(RelativeUriError: JwtGeneratorError)
    }

    "fail if secret length is less than 256 bits" in {
      implicit val ctx = MockAuthContext(atlassianUrl, "INVALID")
      val result = JwtGenerator.generateToken("GET", atlassianUrl)

      result must beLeft(InvalidSecretKey: JwtGeneratorError)
    }

    "generate jwt successfully" in {
      implicit val ctx = MockAuthContext(atlassianUrl)
      val result = JwtGenerator.generateToken("GET", atlassianUrl)

      result must beRight
    }

    "set expiry token correctly" in {
      implicit val acConfig = AcJwtConfig("com.allantl.http4s", 5L)
      implicit val ctx = MockAuthContext(atlassianUrl)
      val jwtToken = JwtGenerator.generateToken("GET", atlassianUrl)

      val expiry =
        jwtToken.right.flatMap(JwtParser.parse).right.map(_.claims.getExpirationTime.getTime / 1000)
      val now = System.currentTimeMillis / 1000
      val expectedExpiry = now + acConfig.jwtExpirationInSeconds

      expiry must beRight
      expiry.right.get must beCloseTo(expectedExpiry +/- 2)
    }
  }
}
