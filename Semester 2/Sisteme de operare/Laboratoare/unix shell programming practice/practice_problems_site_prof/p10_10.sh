#!/bin/bash

if [ $# -eq 0 ]; then
	echo "No args provided"
fi

for file in $@; do
	if [ -f "$file" ]; then
		du -b "$file"
	fi
done | sort -n 
