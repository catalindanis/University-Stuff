#!/bin/bash

for file in $@; do
	echo `grep -E "^#include <.*>$" $file`
done
