package com.allantl.jira4s.v2.domain.enums

import cats.Show

sealed trait UserExpand

object UserExpand {

  case object Groups extends UserExpand
  case object ApplicationRoles extends UserExpand

  implicit val show: Show[UserExpand] = Show.show {
    case Groups => "groups"
    case ApplicationRoles => "applicationRoles"
  }

}
