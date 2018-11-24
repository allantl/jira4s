name := "jira4s"

organization := "com.allantl"

val scala211Version = "2.11.112"
val scala212Version = "2.12.7"

scalaVersion := scala212Version
crossScalaVersions := Seq(scala212Version, scala211Version)

val CirceVersion = "0.10.1"
val AtlassianJwtVersion = "0.1.6"
val Specs2Version = "4.2.0"
val SttpVersion = "1.5.0"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.toolsplus" %% "atlassian-jwt-generators" % AtlassianJwtVersion,
      "io.toolsplus" %% "atlassian-jwt-core" % AtlassianJwtVersion,
      "com.softwaremill.sttp" %% "core" % SttpVersion,
      "com.softwaremill.sttp" %% "circe" % SttpVersion,
      "com.typesafe" % "config" % "1.3.2"
    ),
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % Specs2Version % "it,test",
      "org.specs2" %% "specs2-scalacheck" % Specs2Version % "it,test",
    )
  )
  .settings(parallelExecution in IntegrationTest := false)
