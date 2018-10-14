name := "jira4s"

scalaVersion := "2.12.7"

val CirceVersion = "0.10.0"
val AtlassianJwtVersion = "0.1.5"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "io.toolsplus" %% "atlassian-jwt-generators" % AtlassianJwtVersion,
  "io.toolsplus" %% "atlassian-jwt-core" % AtlassianJwtVersion,
  "io.lemonlabs" %% "scala-uri" % "1.3.1"
)
