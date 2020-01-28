name := "warikan-domain-scala"

version := "0.0.1"

scalaVersion := "2.13.1"

jigModelPattern in jig := ".+\\.domain\\.(model|type)\\.[^$]+"

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.1.0" % "test")