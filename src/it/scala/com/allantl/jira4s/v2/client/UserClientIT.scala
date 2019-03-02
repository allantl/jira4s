package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest
import com.allantl.jira4s.v2.domain.enums.UserExpand

class UserClientIT extends IntegrationTest {

  "UserClient" should {
    "get user by account id" in {

      val user = client.getUserById(
        "557058:d4e3b3c7-0699-401c-8727-2a432c62b5f9",
        Set(UserExpand.Groups, UserExpand.ApplicationRoles)
      )

      val applicationGroupsNonEmpty = user.toOption.flatMap(_.applicationRoles.map(_.items.nonEmpty)).getOrElse(false)
      val groupsNonEmpty = user.toOption.flatMap(_.groups.map(_.items.nonEmpty)).getOrElse(false)

      user must beRight
      applicationGroupsNonEmpty must beTrue
      groupsNonEmpty must beTrue
    }
  }
}
