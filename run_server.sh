#!/bin/bash
managedClasspath="./target/streams/runtime/managedClasspath/\$global/streams/export"
unmanagedClasspath="./target/streams/runtime/unmanagedJars/\$global/streams/export"

# Using server defined in build.sbt
# java -mx4g -cp "$(< $managedClasspath)" edu.stanford.nlp.pipeline.StanfordCoreNLPServer
# TODO: Make this actually work:Using server from lib, i.e. latest from HEAD as of 5 May 2016
java -mx12g -cp "$(< ${unmanagedClasspath}):$(< ${managedClasspath})" edu.stanford.nlp.pipeline.StanfordCoreNLPServer
