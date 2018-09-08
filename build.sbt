
inThisBuild(List(
  organization := "com.example",
  scalaVersion := "2.12.6",
  version      := "0.1.0-SNAPSHOT",
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)
))

lazy val `citi1-api` = project
  .settings(
    libraryDependencies += Libs.`play-json`,
  )

lazy val `citi1-client` = project
  .dependsOn(`citi1-api`)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`akka-http`,
      Akka.`akka-stream`,
      Libs.`akka-http-play-json`,
      Libs.`scala-async`,
      Libs.scalatest % Test,
    )
  )

lazy val `citi1-server` = project
  .dependsOn(`citi1-api`)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`akka-http`,
      Akka.`akka-stream`,
      Libs.`akka-http-play-json`,
      Libs.`scala-async`,
      Libs.scalatest % Test,
    )
  )
