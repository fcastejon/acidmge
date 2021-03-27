#!/bin/bash

echo parameters $*

cd ..
#java -ea -Xms128m -Xmx2048m -Xss4m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -jar lib/acidge.jar $*
java -ea -Xms128m -Xmx2048m -Xss4m -jar lib/acidge.jar $*


