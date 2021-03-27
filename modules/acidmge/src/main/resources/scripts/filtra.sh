#!/bin/bash

cd ../logs

#tail --lines=1000 salida*.log | grep -Pzo "(?s)Mejor.*?Genotipo\N*|salida.*?log" > mejor.txt
#grep Mejor salida*.log > mejor.txt
#grep -Pzo "(?s)Mejor.*Genotipo\N*" salida*.log > mejor.txt

rm -f mejor.txt
for FILE in salida*.log
do
echo $FILE >> mejor.txt 
grep -A 200 Mejor $FILE | grep -Pzo "(?s)Mejor.*?Genotipo\N*" >> mejor.txt
done

head --lines=1000 salida*.log | grep GEProperties > resumen.txt
tail --lines=1000 salida*.log | grep Resumen >> resumen.txt
tail --lines=1000 salida*.log | grep Final > final.txt
grep evolFitness salida*.log > evolFitness.txt

tar cvfz res.tgz mejor.txt final.txt resumen.txt evolFitness.txt

cd ../bin
