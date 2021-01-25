name := """feature-extractor"""

version := "1.0-SNAPSHOT"

// We can instruct sbt-native-packager to use Ash script instead of bash.
lazy val root = (project in file(".")).enablePlugins(PlayJava, AshScriptPlugin, DockerPlugin)

crossPaths := false


scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")



libraryDependencies += guice


libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.196",
  "org.assertj" % "assertj-core" % "3.6.2" % Test,
  "org.awaitility" % "awaitility" % "2.0.0" % Test,
  "javax.xml.bind" % "jaxb-api" % "2.1",
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "org.twitter4j" % "twitter4j-async" % "4.0.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test,
  "org.mockito" % "mockito-core" % "2.18.0" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test

)


// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Javadoc
sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))


// DOCKER SETTINGS

Docker / maintainer := "markglasgow@gmail.com" //
Docker / packageName := "markglasgow/feature-extractor"
Docker / version := sys.env.getOrElse("BUILD_NUMBER", "0")
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts in Docker := Seq(9000, 9443)
dockerBaseImage := "adoptopenjdk:8" // must be installed locally
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true

/// DOCKER SETTINGS END  - sbt docker:publishLocal

// PID


// PID END