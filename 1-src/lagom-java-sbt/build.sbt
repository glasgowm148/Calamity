organization in ThisBuild := "org.cs2021"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"

lazy val `eris` = (project in file("."))
  .aggregate(`eris-api`, `eris-impl`, `eris-stream-api`, `eris-stream-impl`)

lazy val `eris-api` = (project in file("eris-api"))
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `eris-impl` = (project in file("eris-impl"))
  .enablePlugins(LagomJava)
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`eris-api`)

lazy val `eris-stream-api` = (project in file("eris-stream-api"))
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi
    )
  )

lazy val `eris-stream-impl` = (project in file("eris-stream-impl"))
  .enablePlugins(LagomJava)
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaClient,
      lagomLogback,
      lagomJavadslTestKit
    )
  )
  .dependsOn(`eris-stream-api`, `eris-api`)

val lombok = "org.projectlombok" % "lombok" % "1.18.8"

def common = Seq(
  javacOptions in Compile += "-parameters"
)
