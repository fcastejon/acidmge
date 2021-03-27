#!/bin/bash

if [ $# -lt 1 ]
then
echo usage lanza.sh number_of_runs [optional parameters] 
exit -1
fi

RUNS=$1
shift

cd ..
for ((i=1; i<=$RUNS; i++)); do
#echo $i $*
java -ea -Xms128m -Xmx2048m -Xss4m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -cp "lib/acidge.jar:lib/acidmge.jar" es.uned.simda.acidge.ge.Main $*

done




