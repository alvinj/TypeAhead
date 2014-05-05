import AssemblyKeys._

// sbt-assembly
assemblySettings

name := "TypeAhead"
 
version := "0.1"
 
scalaVersion := "2.10.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

// "junit-interface" lets me use ScalaTest from within Eclipse.
// see http://www.scala-sbt.org/release/docs/Detailed-Topics/Testing.html
libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "com.novocode" % "junit-interface" % "0.9" % "test",
    "org.slf4j" % "slf4j-api" % "1.7.7",
    "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1",
    "org.slf4j" % "slf4j-log4j12" % "1.7.7"
)

// "org.slf4j" % "slf4j-simple" % "1.7.7",

