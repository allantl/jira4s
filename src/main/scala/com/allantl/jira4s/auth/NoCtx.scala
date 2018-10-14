package com.allantl.jira4s.auth

sealed trait NoCtx
case object EmptyCtx extends NoCtx

object NoCtx {
  implicit val noOp: NoCtx = EmptyCtx
}
