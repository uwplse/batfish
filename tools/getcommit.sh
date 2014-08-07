#!/usr/bin/env bash

UCLA_CONFIG_PATH=$1
OUTPUT=$2
OLD_PWD=$PWD
cd $UCLA_CONFIG_PATH
{
git log -- core/ distribution/ border/| grep commit | while read line
do
	thiscommit=${line#"commit"}
	echo $thiscommit
done
}>$OUTPUT
cd $OLD_PWD
