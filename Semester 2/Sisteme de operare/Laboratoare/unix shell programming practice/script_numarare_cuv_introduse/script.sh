#!/bin/bash

#acest script citeste input de la tastatura pana la introducerea cuvantului "stop" si afiseaza ulterior
#numarul de cuvinte citite

N=0
while true; do
	read X
	if [ "$X" == "stop" ]; then
		break
	else
		N=`expr $N + $(echo $X | wc -w)`
	fi
done
echo $N
