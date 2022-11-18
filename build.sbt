name := """handson"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
libraryDependencies += jdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.45"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.1.0"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.4.1"
// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.4"

