import AssemblyKeys._ 
assemblySettings

/** Project */
name := "signal-collect-javaapi"

version := "2.0.0-SNAPSHOT"

organization := "com.signalcollect"

scalaVersion := "2.10.0-RC2"

EclipseKeys.withSource := true

/** Dependencies */
libraryDependencies ++= Seq(
 "org.scala-lang" % "scala-library" % "2.10.0-RC2"  % "compile"
  )