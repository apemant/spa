#!/bin/bash
echo "boilerplate.sh "$(date) > log
for f in $(find $1 -iname '*.java') 
do
  if test "$(grep "/\*\*\*\*license" $f)" = ""; then
    echo "/****license*****************************************************************" > _tmp
    echo "**   file: "$(basename $f) >> _tmp
    cat boilerplate.txt >> _tmp
    cat $f >> _tmp
    cat _tmp > $f 
  else
    echo $f" is already processed!" >> log
  fi
done
