package com.allantl.jira4s.auth.jwt.errors

abstract class JwtGeneratorError(msg: String) extends Exception(msg)
case object RelativeUriError extends JwtGeneratorError("Url must be absolute")
case object InvalidSigningError extends JwtGeneratorError("Error when signing jwt")
case object BaseUrlMismatchError extends JwtGeneratorError("Invalid host base url")
case object InvalidUriError extends JwtGeneratorError("Url is not valid")
