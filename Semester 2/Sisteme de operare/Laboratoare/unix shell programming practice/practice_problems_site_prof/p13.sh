#!/bin/bash

if [ $# -eq 0 ]; then
	echo "No args provided"
	exit 1
fi

if [ ! -d $1 ]; then
	echo "Arg provided is not a dir"
  	exit 1
fi	

for file in `find $1 -type l`; do
	if [ ! -e $file ]; then
		echo "Link doesn't exist"
	fi
done
