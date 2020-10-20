// Docker PID permissions
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)
name := """help-me-event-detection"""
organization := "com.helpme"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies += guice


// Docker

Docker / maintainer := "2336003g@student.gla.ac.uk" // TODO: set your info here
Docker / packageName := "help-me-event-detection"
Docker / version := sys.env.getOrElse("BUILD_NUMBER", "0")
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:15"
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true