
counter=0
for A in $@; do
	counter=`expr $counter + 1`
done
echo "Number of args is: $counter"

