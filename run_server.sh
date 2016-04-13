#!/bin/bash
java -mx4g -cp "$(< ./target/streams/runtime/managedClasspath/\$global/streams/export)" edu.stanford.nlp.pipeline.StanfordCoreNLPServer &> server.log
