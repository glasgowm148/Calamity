import play.core.PlayVersion

name := """helpme-akka"""
organization := "com.helpme"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)


libraryDependencies += guice

// In order to use Akka Cluster SBR-OSS for the cluster we need to override the Akka version
// and use, at least, Akka 2.6.6. So, instead of using the Akka artifacts transitively provided
// by Play we override them.
// val akkaVersion =  PlayVersion.akkaVersion
val akkaVersion =  "2.6.6"

// Some Akka overrides to align versions of artifacts
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
)

// this dependency is required to form the Akka Cluster
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion

// Sending messages from a node to another in the Akka Cluster requires serializing. This
// example application uses the default Akka Jackson serializer with the CBOR format.
// See also `conf/serialization.conf` and `services.CborSerializable` for more info.
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion


// Docker PID permissions
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)


// version := "1.0-SNAPSHOT"


scalaVersion := "2.13.3"

libraryDependencies += guice


// Docker
maintainer := "2336003g@student.gla.ac.uk"
Docker / maintainer := "2336003g@student.gla.ac.uk" // TODO: set your info here
Docker / packageName := "helpme-akka"
Docker / version := sys.env.getOrElse("BUILD_NUMBER", "1.0-SNAPSHOT")
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:9.0.4"
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true

import com.typesafe.sbt.packager.MappingsHelper._
mappings in Universal ++= directory(baseDirectory.value / "../data/raw/tweets")