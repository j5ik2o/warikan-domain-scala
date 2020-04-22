name := "warikan-domain-scala"

version := "0.0.1"

scalaVersion := "2.13.1"

jigModelPattern in jig := ".+\\.domain\\.(model|type)\\.[^$]+"

jigReports in jig := ((jigReports in jig).dependsOn(compile in Compile)).value

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.1.0" % "test")

