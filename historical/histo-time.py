#!/usr/bin/python
import fileinput
import matplotlib.pyplot as plt
import sys

x = []
sumint=0
maxint=0
minint=150*24*3600

filename=sys.argv[1]
f = open(filename,'r')
propertyname=filename[:-5]

for line in f.readlines():
	interval=float(line)/3600
	x.append(interval)
	sumint+=interval
	if interval > maxint:
		maxint=interval
	if interval < minint:
		minint=interval


xlable = "Time between changes/h"
ylable = "Changes Count"
title = "Changes histogram - " + propertyname
pngname = propertyname + "-histo.png"

plt.xlabel(xlable)
plt.ylabel(ylable)
plt.title(title)
plt.hist(x,bins=30)
plt.savefig(pngname)
