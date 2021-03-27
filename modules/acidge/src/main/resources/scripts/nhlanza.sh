#!/bin/bash

if [ $# -lt 1 ]
then
echo usage nhlanza.sh number_of_runs [optional parameters] 
exit -1
fi

RUNS=$1
shift

echo $RUNS $*
nohup ./lanza.sh $RUNS $* < /dev/null > ../logs/consola.log 2>&1 &

