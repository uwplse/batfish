#!/usr/bin/env bash

NEWPWD=$1
OLDPWD=$PWD
cd $NEWPWD
for i in history-cmpres-*
do
	echo processing...$i
	output=$i.manual
	$OLDPWD/manual.py $i > $output
done
cd $OLDPWD
