package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest

class PriorityClientIT extends IntegrationTest {

  "PriorityClientIT" should {
    "get all priorities" in {
      val priorities = client.getPriorities()
      priorities must beRight
    }

    "get priority by id" in {
      val res =
        for {
          priorities <- client.getPriorities().right.toOption
          id <- priorities.headOption.map(_.id)
          priority <- client.getPriority(id).right.toOption
        } yield priority

      res must beSome
    }
  }
}
