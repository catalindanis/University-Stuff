#!/bin/bash

while [ $# -gt 0 ]; do
	if [ -d $1 ]; then
		echo "$1 directory"
	elif [ -f $1 ]; then
		echo "$1 file"
	elif [ `echo $1 | grep -E "^[0-9]+$"` ]; then
		echo "$1 number"
	else
		echo "$1 something else"
	fi
	shift	
done
