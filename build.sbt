name := "jira4s"

scalaVersion := "2.12.7"

val CirceVersion = "0.10.0"
val AtlassianJwtVersion = "0.1.5"
val Specs2Version = "4.2.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "io.toolsplus" %% "atlassian-jwt-generators" % AtlassianJwtVersion,
  "io.toolsplus" %% "atlassian-jwt-core" % AtlassianJwtVersion,
  "io.lemonlabs" %% "scala-uri" % "1.3.1",
  "com.softwaremill.sttp" %% "core" % "1.3.8",
  "com.softwaremill.sttp" %% "circe" % "1.3.8"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % Specs2Version % "test",
  "org.specs2" %% "specs2-scalacheck" % Specs2Version % "test",
)
