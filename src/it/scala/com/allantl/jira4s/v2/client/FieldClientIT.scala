package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest

class FieldClientIT extends IntegrationTest {

  "Field Client" should {
    "fetch fields"  in {
      val fields = client.getFields()
      fields must beRight
    }
  }

}
