import ReleaseTransformations._

name := "jira4s"
organization in ThisBuild := "com.github.allantl"
homepage in ThisBuild := Some(url("https://github.com/allantl/jira4s"))
licenses in ThisBuild := List("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

val scala211Version = "2.11.12"
val scala212Version = "2.12.7"

scalaVersion := scala212Version
crossScalaVersions := Seq(scala212Version, scala211Version)

val CirceVersion = "0.11.0"
val AtlassianJwtVersion = "0.1.6"
val Specs2Version = "4.2.0"
val SttpVersion = "1.5.0"

lazy val publishSettings = Seq(
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  autoAPIMappings := true,
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/allantl/jira4s"),
      "scm:git:git@github.com:allantl/jira4s.git"
    )
  ),
  developers := List(
    Developer(
      "allantl",
      "Allan Timothy Leong",
      "allan.timothy.leong@gmail.com",
      url("https://github.com/allantl")
    )
  )
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

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
  .settings(publishSettings)
