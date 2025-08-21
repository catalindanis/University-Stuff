n=0
for f in `find dir -type f -name "*.log"`; do
	n=`expr $n + 1`
	cat "$f" | sort > "$f.temp" && mv "$f.temp" "$f"
done
echo $n "files modified"
