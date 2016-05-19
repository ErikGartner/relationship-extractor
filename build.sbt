name := """relationshipextrator"""

version := "0.2"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.0.0-beta-2"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.21"


// We use Play frameworks JSON library
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"

// Graphstream used for outputting a graph
libraryDependencies += "org.graphstream" % "gs-core" % "1.3"
libraryDependencies += "org.graphstream" % "gs-ui" % "1.3"
libraryDependencies += "org.graphstream" % "gs-algo" % "1.3"

// Easy rules for inferring relationships from the graph
libraryDependencies += "org.easyrules" % "easyrules-core" % "2.2.0"
libraryDependencies += "org.easyrules" % "easyrules" % "2.2.0"


