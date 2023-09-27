import Dependencies._

ThisBuild / scalaVersion     := "2.13.11"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "akka-practices",
    libraryDependencies += munit % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.9.0-M1"
// https://mvnrepository.com/artifact/com.typesafe.play/play-json
//libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.1"


libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.3"
