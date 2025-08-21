#!/bin/bash

if [ $# -eq 0 ]; then
	echo "No args provided"
	exit 1
fi

if [ ! -d "$1" ]; then
	echo "Arg is not a directory"
	exit 1
fi

find "$1" -type f | awk -F/ '{print $NF}' | sort | uniq -c

