#!/bin/bash

if [ $# -lt 1 ]
then
echo usage nhserver.sh number_of_servers [optional parameters] 
exit -1
fi

SERVERS=$1
shift
echo Starting $SERVERS servers

cd ..
for ((i=0; i<$SERVERS; i++)); do
echo $i $*
nohup java -ea -Xms128m -Xmx1024m -Xss4m -jar lib/acidge.jar -server -s $i  $* < /dev/null > logs/consola_$i.log 2>&1 &
sleep 1
done


