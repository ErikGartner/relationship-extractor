#!/bin/bash
managedClasspath="./target/streams/runtime/managedClasspath/\$global/streams/export"
unmanagedClasspath="./target/stream/runtime/unmanagedJars/\$global/streams/export"
# Using server defined in build.sbt
java -mx4g -cp "$(< $managedClasspath)" edu.stanford.nlp.pipeline.StanfordCoreNLPServer
# TODO: Make this actually work:Using server from lib, i.e. latest from HEAD as of 5 May 2016
# java -mx4g -cp "$(< $unmanagedJars):$(< $managedClasspath)" edu.stanford.nlp.pipeline.StanfordCoreNLPServer