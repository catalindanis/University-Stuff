#!/bin/bash

df | awk '{print $2, $5, $6}' | tail -n+2 | while read s m r; do
	m=`echo $m | sed -E "s/%//"`
        if [ $s -lt 1000000 ] || [ `expr 100 - $m` -lt 20 ]; then
		echo $r
	fi
done
