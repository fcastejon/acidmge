#!/bin/bash
#
# Para pruebas de timeout, es un bucle infinito

echo parametros $*
/usr/local/bin/ngspice $*

if [ "$1" = "" ]
then
echo uso: longspice netlist
exit 1
fi

while true
do
echo Soy un bucle infinito
sleep 1
done