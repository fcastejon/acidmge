#!/bin/bash

echo parametros $*
echo JAVA_HOME=$JAVA_HOME


cd ..
java -ea -Xms128m -Xmx1024m -Xss4m -jar lib/acidge.jar -server $*


