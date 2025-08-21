c=0
for f in `find dir -type f -name "*.c"`; do
	n=`wc -l < "$f"`
	#echo $n
	if [ $n -gt 500 ]; then
		c=`expr $c + 1`
		echo $f
	fi
	if [ $c -eq  2 ]; then
		break;
	fi
done

