# Jira4s

[![Build Status](https://travis-ci.org/allantl/jira4s.svg?branch=master)](https://travis-ci.org/allantl/jira4s)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.allantl/jira4s_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.allantl/jira4s_2.12)

[Atlassian Jira Client](https://developer.atlassian.com/cloud/jira/platform/rest/v2/) for Scala

## Dependencies

- [Cats](https://github.com/typelevel/cats)
- [Sttp](https://github.com/softwaremill/sttp)
- [Circe](https://github.com/circe/circe)
- [Typesafe Config](https://github.com/typesafehub/config)
- [Atlassian JWT Scala](https://github.com/toolsplus/atlassian-jwt)

## Getting Started

Add the following to your build.sbt:
```
libraryDependencies += "com.github.allantl" %% "jira4s" % "0.2.0"
```

## Usage

This library exposes two types of client
- `JiraSingleTenantClient` for authentication using api token and basic auth.
- `JiraMultiTenantClient` for [Atlassian Connect](https://developer.atlassian.com/static/connect/docs/index.html) add-on and OAuth(not supported yet).

To initialize the client with authentication using `ApiToken` or `BasicAuthentication`

```scala
import cats.Id
import com.allantl.jira4s.auth.{ApiToken, BasicAuthentication}
import com.allantl.jira4s.v2.JiraSingleTenantClient
import com.softwaremill.sttp.HttpURLConnectionBackend

object Jira4S extends App {
    implicit val sttpBackend = HttpURLConnectionBackend()

    // ApiToken
    val client: JiraSingleTenantClient[Id] = JiraSingleTenantClient(
        ApiToken(
          "jiraUrl",
          "email",
          "apiToken"
        )
    )

    // Basic Auth
    val client2: JiraSingleTenantClient[Id] = JiraSingleTenantClient(
        BasicAuthentication(
          "jiraUrl",
          "email",
          "password"
        )
    )

    val issue = client.getIssue("10000")

}
```

If you are developing an Atlassian Connect add-on

```scala
import cats.Id
import com.allantl.jira4s.auth.{AcJwtConfig, AuthContext}
import com.allantl.jira4s.v2.JiraMultiTenantClient
import com.softwaremill.sttp.HttpURLConnectionBackend

object Jira4S extends App {

  implicit val sttpBackend = HttpURLConnectionBackend()

  val jwtExpirationInSeconds = 5L
  val client: JiraMultiTenantClient[Id] = JiraMultiTenantClient(
    AcJwtConfig(
      "addOnKey",
      jwtExpirationInSeconds
    )
  )

  // You need to specify an implicit AuthContext when making a request to Jira
  case class UserCtx(instanceUrl: String, accessToken: String) extends AuthContext

  implicit val userCtx = UserCtx(
    "http://your-domain.atlassian.net",
    "atlassian-host-shared-secret"
  )

  val issue = client.getIssue("10000")

}

```

## Configuration

You can add configuration using environment variables or HOCON in application config.
Then, you can initialize the client with

```scala
import com.allantl.jira4s.v2.{JiraMultiTenantClient, JiraSingleTenantClient}
import com.softwaremill.sttp.HttpURLConnectionBackend

object Jira4S extends App {

  implicit val sttpBackend = HttpURLConnectionBackend()

  val singleTenantClient = JiraSingleTenantClient()
  val multiTenantClient = JiraMultiTenantClient()

}

```

### Api Token:
```
jira4s {
  jira-url = "https://your-domain.atlassian.net"
  username = "username"
  api-token = "api-token"
}
```

Environment Variables:
```
export JIRA4S_JIRA_URL = "https://your-domain.atlassian.net"
export JIRA4S_USERNAME = "username"
export JIRA4S_API_TOKEN = "api-token"
```

### Basic Authentication
```
jira4s {
  jira-url = "https://your-domain.atlassian.net"
  username = "username"
  password = "password"
}
```

Environment Variables:
```
export JIRA4S_JIRA_URL = "https://your-domain.atlassian.net"
export JIRA4S_USERNAME = "username"
export JIRA4S_PASSWORD = "password"
```

### Atlassian connect add-on
```
jira4s {
  add-on-key = "Your add on key"
  jwt-expiration-in-seconds = 5
}
```

Environment Variables:
```
export JIRA4S_ADD_ON_KEY = "Your add on key"
export JIRA4S_JWT_EXPIRATION_IN_SECONDS = 5
```

## Backend
Sttp supports different type of backends which return different type of effects. Please take a look at [sttp docs](https://sttp.readthedocs.io/en/latest/backends/summary.html).

## Logging

To add logging to the client, please refer [here](https://sttp.readthedocs.io/en/latest/backends/custom.html).
