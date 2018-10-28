package com.allantl.jira4s

import com.softwaremill.sttp.{RequestBody, StringBody}
import io.circe.Json
import io.circe.parser._

import scala.io.{Codec, Source}

package object utils {

  def loadJson(fileLocation: String): String =
    Source.fromURL(getClass.getResource(s"/fixtures/v2/$fileLocation"))(Codec.UTF8).mkString

  def assertStringBody[S](req: RequestBody[S])(assert: Map[String, Json] => Boolean): Boolean =
    req match {
      case StringBody(s, _, _) =>
        parse(s).toOption.flatMap(_.as[Map[String, Json]].toOption).fold(false)(assert)

      case _ =>
        false
    }
}
