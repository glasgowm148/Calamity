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
scalaVersion := "2.12.10"




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
dockerBaseImage := "openjdk:8"
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
  "org.hibernate" % "hibernate-entitymanager" % "3.6.10.Final",
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "redis.clients" % "jedis" % "2.6.2"
)

/*
 "org.springframework" % "spring-context" % "3.2.2.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "org.springframework.data" % "spring-data-jpa" % "1.3.2.RELEASE",
  "org.springframework" % "spring-expression" % "3.2.2.RELEASE",
 */

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

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.1"


// CoreNLP
libraryDependencies ++= Seq(
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"
)


// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "3.0.1" % "provided"

// https://mvnrepository.com/artifact/edu.berkeley.nlp/berkeleylm
libraryDependencies += "edu.berkeley.nlp" % "berkeleylm" % "1.1.2"

// https://mvnrepository.com/artifact/org.nd4j/nd4j-api
libraryDependencies += "org.nd4j" % "nd4j-api" % "1.0.0-beta7"

// https://mvnrepository.com/artifact/org.deeplearning4j/deeplearning4j-core
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta7"

// https://mvnrepository.com/artifact/org.apache.opennlp/opennlp-tools
libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.9.3"

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.11"
