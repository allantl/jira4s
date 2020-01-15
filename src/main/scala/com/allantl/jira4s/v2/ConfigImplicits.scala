package com.allantl.jira4s.v2

import com.typesafe.config.{Config => TypesafeConfig}

import scala.util.{Properties, Try}

private[jira4s] object ConfigImplicits {

  private def getEnv(variable: String): Option[String] =
    Properties.envOrNone(s"JIRA4S_$variable")

  private def getConfig(path: String)(implicit config: TypesafeConfig): Option[String] =
    Try(config.getString(s"jira4s.$path")).toOption

  implicit class Tuple2Conf(tuple: (String, String)) {
    def loadConfig[T](
        f: (String, String) => Option[T]
    )(implicit config: TypesafeConfig): Option[T] =
      for {
        c1 <- getConfig(tuple._1)
        c2 <- getConfig(tuple._2)
        res <- f(c1, c2)
      } yield res

    def loadEnv[T](f: (String, String) => Option[T]): Option[T] =
      for {
        e1 <- getEnv(tuple._1)
        e2 <- getEnv(tuple._2)
        res <- f(e1, e2)
      } yield res
  }

  implicit class Tuple3Conf(tuple: (String, String, String)) {
    def loadConfig[T](
        f: (String, String, String) => Option[T]
    )(implicit config: TypesafeConfig): Option[T] =
      for {
        c1 <- getConfig(tuple._1)
        c2 <- getConfig(tuple._2)
        c3 <- getConfig(tuple._3)
        res <- f(c1, c2, c3)
      } yield res

    def loadEnv[T](f: (String, String, String) => Option[T]): Option[T] =
      for {
        e1 <- getEnv(tuple._1)
        e2 <- getEnv(tuple._2)
        e3 <- getEnv(tuple._3)
        res <- f(e1, e2, e3)
      } yield res
  }

}
