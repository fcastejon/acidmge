#!/bin/bash

echo parametros $*
/usr/local/bin/ngspice $*

exit $?

