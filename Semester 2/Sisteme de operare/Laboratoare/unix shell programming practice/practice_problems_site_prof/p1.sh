#!/bin/bash

awk '{print $1}' who.fake |  while read name; do
	echo $name `grep -E "^$name" ps.fake | wc -l`
done	


