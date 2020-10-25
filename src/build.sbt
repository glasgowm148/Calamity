import play.core.PlayVersion
import com.typesafe.sbt.packager.MappingsHelper._
mappings in Universal ++= directory(baseDirectory.value / "../data/raw/tweets")
// Docker PID permissions
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)


maintainer := "2336003g@student.gla.ac.uk"
Docker / maintainer := "2336003g@student.gla.ac.uk"
Docker / packageName := "helpme-akka"
Docker / version := sys.env.getOrElse("BUILD_NUMBER", "1.0-SNAPSHOT")
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:9.0.4" // services.CounterActor class file has version 57
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true

name := """helpme-akka"""
organization := "com.helpme"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)


// In order to use Akka Cluster SBR-OSS for the cluster we need to override the Akka version
// and use, at least, Akka 2.6.6. So, instead of using the Akka artifacts transitively provided
// by Play we override them.
val akkaVersion =  PlayVersion.akkaVersion
//val akkaVersion =  "2.6.6"
scalaVersion := "2.13.3"

// Dependencies
libraryDependencies ++= Seq(
  ws,
  guice,
  javaWs,
  javaCore,
  javaJpa,
  "org.springframework" % "spring-context" % "3.2.2.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "org.springframework.data" % "spring-data-jpa" % "1.3.2.RELEASE",
  "org.springframework" % "spring-expression" % "3.2.2.RELEASE",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.10.Final",
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "redis.clients" % "jedis" % "2.6.2",
  // https://mvnrepository.com/artifact/net.sf.trove4j/trove4j
  "net.sf.trove4j" % "trove4j" % "3.0.3",
  "org.parceler" % "parceler-api" % "1.1.13",
  // this dependency is required to form the Akka Cluster
  "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,
  "com.google.code.gson" % "gson" % "2.2.4",
  "com.typesafe.play" %% "play-json" % "2.9.1",
  // Some Akka overrides to align versions of artifacts,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
)











