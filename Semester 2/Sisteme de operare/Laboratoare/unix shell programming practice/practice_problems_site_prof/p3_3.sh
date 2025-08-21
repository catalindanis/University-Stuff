#!/bin/bash

if [ $# -le 0 ]; then
	exit 1	
fi

if [ ! -d $1 ]; then
	exit 1
fi

N=0
for file in `find $1 -type f -name "*.c"`; do
	nr_lines=`grep -E -c -v "^[[:space:]]$" "$file"`
	echo "$file : $nr_lines"
	N=`expr $N + $nr_lines`
done

echo $N
