#!/bin/bash 

for path in `find $1 -type f`; do
	#N=`ls -l "$path" | awk '{print $1}' | grep -E ".r..r..r.."`	
	perm=`ls -l "$path" | awk '{print $1}' | grep -E "..w..w..w."`
	if [ $? -eq 0 ]; then
		chmod a-w "$path"
		newperm=`echo $perm | sed -E "s/w/\-/g"`
		echo "$path" "$perm" "$newperm"
	fi
done
