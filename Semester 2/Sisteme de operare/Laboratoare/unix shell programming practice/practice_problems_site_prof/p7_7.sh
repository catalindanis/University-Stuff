#!/bin/bash

if [ $# -le 0 ]; then
	echo "No arguments provided!"
	exit 1
fi

if [ ! -d $1 ]; then
	echo "Argument given is not a directory!"
	exit 1
fi

sum=0
for arg in `ls $1`; do
	if [ -f "$1/$arg" ]; then
		size=`du -b "$1/$arg" | awk '{print $1}'`
		sum=$((sum+size))
	fi
done

echo $sum
