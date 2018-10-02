#!/bin/bash

# mvn compile

cpJAVA=\
target/classes\
:src/main/resources/soot-3.1.0.jar

# cpSOOT=\
# target/classes\
# :/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/rt.jar\
# :/usr/share/java/junit-3.8.2.jar\
# :/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jce.jar

cpSOOT=\
target/classes\
:target/lib/rt-1.8.0.jar\
:target/lib/junit-3.8.2.jar\
:target/lib/jce-1.8.0.jar

#     The JVM gets this                     ...     and Soot gets this
# java -cp $cpJAVA eda045f.exercises.MyMainClass -cp $cpSOOT -f j eda045f.exercises.test.Foo

# With Maven package:
mvn compile
# for filename in ./examples/*; do
#     if [[ -f $filename ]]; then
#         echo "----------------------------------------------"
#         echo "Running for: $filename"
#         echo "----------------------"
#         java -cp target/classes:target/lib/* eda045f.exercises.MyMainClass -cp $cpSOOT -f jimple -process-dir $filename
#         echo "----------------------------------------------"
#     fi
# done

# mvn compile
# mvn package
java -cp target/classes:target/lib/* eda045f.exercises.MyMainClass -cp $cpSOOT -f j eda045f.exercises.test.ArrayIndex
# java -cp target/DFAnalysis1-1.0-SNAPSHOT.jar:target/lib/* eda045f.exercises.MyMainClass -cp $cpSOOT -f j -p jb preserve-source-annotations eda045f.exercises.test.Foo
