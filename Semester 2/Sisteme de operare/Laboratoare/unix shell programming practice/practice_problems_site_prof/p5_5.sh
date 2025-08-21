#!/bin/bash

while read input; do
	if [ -f $input ]; then
		break
	fi
done
