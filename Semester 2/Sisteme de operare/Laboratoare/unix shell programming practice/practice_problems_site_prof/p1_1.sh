#!/bin/bash

find $1 -type f -name "*.c" | grep -E -c ''
