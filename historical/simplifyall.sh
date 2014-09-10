#!/usr/bin/env bash

NEWPWD=$1
OLDPWD=$PWD
cd $NEWPWD
for i in *
do
	output=$i.simplified
{
	$OLDPWD/simplify.py $i | while read line
	do
		echo $line | sed 's_:MapEntry(.*)__g'| sed 's_:ListElement(.*)__g'|sed 's_:SetElement(.*)__g' | sed 's/:[^_]*/./' | sed 's/:[^_]*$//' 
	done
}	 > $output
done
cd $OLDPWD
