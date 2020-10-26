import play.core.PlayVersion

import com.typesafe.sbt.packager.MappingsHelper._
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy

lazy val root = (project in file(".")).enablePlugins(PlayJava)

mappings in Universal ++= directory(baseDirectory.value / "../data/raw/tweets")

name := """helpme-akka"""
organization := "com.helpme"
version := "1.0-SNAPSHOT"

val akkaVersion =  "2.6.6" // val akkaVersion =  PlayVersion.akkaVersion
scalaVersion := "2.13.3"




// Docker PID permissions
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
dockerBaseImage := "openjdk:13"
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true

// version := "1.0-SNAPSHOT"


libraryDependencies += guice

// In order to use Akka Cluster SBR-OSS for the cluster we need to override the Akka version



// Some Akka overrides to align versions of artifacts
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
)

libraryDependencies ++= Seq(
  ws,
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
  "redis.clients" % "jedis" % "2.6.2"
)

// https://mvnrepository.com/artifact/net.sf.trove4j/trove4j
libraryDependencies += "net.sf.trove4j" % "trove4j" % "3.0.3"

libraryDependencies += "org.parceler" % "parceler-api" % "1.1.13"
// this dependency is required to form the Akka Cluster
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion

// Sending messages from a node to another in the Akka Cluster requires serializing. This
// example application uses the default Akka Jackson serializer with the CBOR format.
// See also `conf/serialization.conf` and `services.CborSerializable` for more info.
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion

//
libraryDependencies += "com.google.code.gson" % "gson" % "2.2.4"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.1"




libraryDependencies += guice



