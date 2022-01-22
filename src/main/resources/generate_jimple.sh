#!/bin/bash

SOOT_JAR='/Users/alex/projects/java_proj/static_analysis_lab/flow_analysis/src/main/resources/soot-4.2.1-jar-with-dependencies.jar'
javac $1.java
java -cp $SOOT_JAR soot.Main -cp . -pp $1 -f jimple

# bash –process-dir
# $ java -cp $SOOT_JAR soot.org.xpectuer.Main -cp . -pp -process-dir . -d /tmp/sootout

