#!/bin/bash

for file in `find $1`; do
	if [ -L file -a -e file ]; then
		echo $file
	fi
done
