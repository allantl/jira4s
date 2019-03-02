package com.allantl.jira4s.v2.client

import com.allantl.jira4s.v2.IntegrationTest
import com.allantl.jira4s.v2.domain.enums.UserExpand

class UserClientIT extends IntegrationTest {

  "UserClient" should {
    "get myself and user by account id" in {

      val myself = client.getCurrentUser()
      myself must beRight

      val user = client.getUserById(
        myself.map(_.accountId).getOrElse(""),
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
