#!/bin/bash

while true; do
    read file

    if [ $file == "stop" ]; then
	  break
    fi  

    if [ -f "$file" ]; then
	    if file "$file" | grep -E -q "text"; then
             	nowords=`head -1 "$file" | wc -w`
	    	echo $nowords
	    fi
    fi
done

echo "stop entered! stopping..."
