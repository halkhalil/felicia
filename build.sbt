name := "felicia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "org.webjars" % "jquery" % "3.1.1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1"
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.5"

