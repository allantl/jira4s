package com.allantl.jira4s.v2

import com.softwaremill.sttp.HttpURLConnectionBackend
import org.specs2.mutable.Specification

trait IntegrationTest extends Specification with Client

trait Client {
  implicit val backend = HttpURLConnectionBackend()
  val client = JiraSingleTenantClient()
}
