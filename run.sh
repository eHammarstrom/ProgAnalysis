#!/bin/bash

cpJAVA=\
target/classes\
:src/main/resources/soot-3.1.0.jar

cpSOOT=\
target/classes\
:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/rt.jar\
:/usr/share/java/junit-3.8.2.jar\
:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jce.jar

#     The JVM gets this                     ...     and Soot gets this
java -cp $cpJAVA eda045f.exercises.MyMainClass -cp $cpSOOT -f j eda045f.exercises.test.Foo
