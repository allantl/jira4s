package com.allantl.jira4s.mocks

import java.util.UUID
import com.allantl.jira4s.auth.AuthContext

case class MockAuthContext(
    instanceUrl: String,
    accessToken: String = UUID.randomUUID().toString
) extends AuthContext
