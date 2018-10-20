package com.allantl.jira4s.auth.jwt.domain

import io.toolsplus.atlassian.jwt.api.CanonicalHttpRequest

case class JwtCredentials(rawJwt: String, canonicalHttpRequest: CanonicalHttpRequest)
