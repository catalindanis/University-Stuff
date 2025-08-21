#!/bin/bash

if [ $# -le 1 ]; then
	echo "Provide more arguments"
	exit 1
fi

if [ $(($# % 2)) -eq 1 ]; then
	echo "Please provide a even number of args!"
	exit 1
fi

while $# -gt 0; do
	n=`grep -E -o "\<$2\>" $1 | wc -l`
	if [ n -ge 3 ]; then
		echo "$2 appears more than 3 times in $1"
	fi
	shift 2
done
