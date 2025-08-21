#!/bin/bash

while true; do
   for dangerous in $@; do
	#ps -ef | grep -E "\<$dangerous\>" | awk '{print $2, $8}' | while read pid name; do
	ps -ef | awk '{print $2, $8}' | while read pid name; do	
	   	if [ "$name" == "$dangerous" ]; then
			#echo "Killing process $dangerous!"
		#else
			#echo "Process not dangerous $name!"
			kill -9 "$pid"
		fi
		# kill -9 "$pid" 
	done
   done
   sleep 1
done
