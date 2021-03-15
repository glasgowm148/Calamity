import play.core.PlayVersion

name := """play-feature-api"""
organization := "com.example"

// Stops the build if JVM != 1.8
initialize := {
  val _ = initialize.value // run the previous initialization
  val required = "1.8"
  val current  = sys.props("java.specification.version")
  assert(current == required, s"Unsupported JDK: java.specification.version $current != $required")/
}

// Docker PID permissions
//dockerChmodType := DockerChmodType.UserGroupWriteExecute
//dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
//javaOptions in Universal ++= Seq(
//  "-Dpidfile.path=/dev/null"
//)

//maintainer := "2336003g@student.gla.ac.uk"
//Docker / maintainer := "2336003g@student.gla.ac.uk"
//Docker / packageName := "play-feature-api"
//Docker / version := sys.env.getOrElse("BUILD_NUMBER", "1.0-SNAPSHOT")
//Docker / daemonUserUid  := None
//Docker / daemonUser := "daemon"
//dockerExposedPorts := Seq(9000, 9443)
//dockerBaseImage := "adoptopenjdk:8"
//dockerRepository := sys.env.get("ecr_repo")
//dockerRepository := Some("registry.gitlab.com/my-gitlab-project")
//dockerUpdateLatest := true

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.4"

libraryDependencies += guice


val akkaVersion =  PlayVersion.akkaVersion

// Some Akka overrides to align versions of artifacts
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
)



libraryDependencies += "commons-io" % "commons-io" % "2.8.0"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.10.8"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.5.32"

// https://mvnrepository.com/artifact/com.twitter.twittertext/twitter-text
libraryDependencies += "com.twitter.twittertext" % "twitter-text" % "3.1.0"

// https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp
//libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "4.2.0"

// CoreNLP
libraryDependencies ++= Seq(
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"
)

// https://mvnrepository.com/artifact/com.github.chen0040/java-text-embedding
libraryDependencies += "com.github.chen0040" % "java-text-embedding" % "1.0.1"




libraryDependencies ++= Seq(
  ws,
  javaWs,
  javaCore,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.4.24.Final",
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "redis.clients" % "jedis" % "2.6.2"
)

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.11"

// Mockito
// https://mvnrepository.com/artifact/org.mockito/mockito-core
libraryDependencies += "org.mockito" % "mockito-core" % "3.8.0" % Test


// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.12.0"
// https://mvnrepository.com/artifact/org.apache.commons/commons-text
libraryDependencies += "org.apache.commons" % "commons-text" % "1.9"
