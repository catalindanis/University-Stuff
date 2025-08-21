#!/bin/bash

if [ $# -eq 0 ]; then
	echo No args provided
	exit 1
fi

if [ ! -f $1 ]; then
	echo "Arg not file"
	exit 1
fi

previous=`sha1sum $1`
while true; do
	current=`sha1sum $1`
	if [ "$previous" != "$current" ]; then
		echo "$previous != $current"
	fi
	previous=$current	
	sleep 1
done
