name := """text-miner"""

version := "0.1"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.0.0-beta-2"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"


// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

