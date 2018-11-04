package com.allantl.jira4s

import com.allantl.jira4s.v2.domain.errors.JiraError

package object IT {

  type JiraErrorOr[A] = Either[JiraError, A]

}
