package com.allantl.jira4s.v2.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class WorkLog(
                    id: String,
                    self: String,
                    author: Author,
                    updateAuthor: Author,
                    comment: String,
                    created: String,
                    updated: String,
                    started: String,
                    timeSpent: String,
                    timeSpentSeconds: Int
                  )

object WorkLog {
  implicit val issueDecoder: Decoder[WorkLog] = deriveDecoder
  implicit val issueEncoder: Encoder[WorkLog] = deriveEncoder
}
