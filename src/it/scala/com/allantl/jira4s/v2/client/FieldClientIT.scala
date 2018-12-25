package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest

class FieldClientIT extends IntegrationTest {

  "FieldClient" should {
    "get fields" in {
      val fields = client.getFields()
      fields must beRight
    }
  }
}
