#!/bin/bash

for f in $@; do
	if [ -f $f ]; then
		du -b $f
	fi
done | sort -n

