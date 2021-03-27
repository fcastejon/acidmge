#!/bin/bash

echo parameters $*

cd ..
java -ea -Xms128m -Xmx2048m -Xss4m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -cp "lib/acidge.jar:lib/acidmge.jar" es.uned.simda.acidge.ge.Main $*


