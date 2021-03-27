#!/bin/bash

echo parametros $*
echo JAVA_HOME=$JAVA_HOME


cd ..
java -ea -Xms128m -Xmx1024m -Xss4m -cp "lib/acidge.jar:lib/acidmge.jar" es.uned.simda.acidge.ge.Main -server $*


