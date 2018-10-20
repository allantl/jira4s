package com.allantl.jira4s.auth.jwt.domain

import java.net.URI

import io.lemonlabs.uri.Url
import io.toolsplus.atlassian.jwt.api.CanonicalHttpRequest

case class CanonicalURIHttpRequest(httpMethod: String, uri: URI) extends CanonicalHttpRequest {

  override def method: String = httpMethod

  override def relativePath: String = {
    val relPath = uri.getPath
    if (relPath.isEmpty) "/" else relPath
  }

  override def parameterMap: Map[String, Seq[String]] =
    Url.parse(uri.toString).query.paramMap
}
