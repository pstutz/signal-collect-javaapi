import sbt._
import Keys._

object JavaApiBuild extends Build {
   lazy val scCore = ProjectRef(file("../signal-collect"), id = "signal-collect")
   val scGraphs = Project(id = "signal-collect-javaapi",
                         base = file(".")) dependsOn(scCore)
}