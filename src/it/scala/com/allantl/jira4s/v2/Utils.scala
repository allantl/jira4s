package com.allantl.jira4s.v2

import scala.util.Random

object Utils {

  def randomStr(length: Int): String =
    Random.alphanumeric.dropWhile(_.isDigit).take(length).mkString("")

}
