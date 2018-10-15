package com.allantl.jira4s.auth

sealed trait NoCtx extends AuthContext
case object EmptyCtx extends NoCtx {
  override val instanceUrl = ""
  override val accessToken = ""
}

object NoCtx {
  implicit val noOp: NoCtx = EmptyCtx
}
