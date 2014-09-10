#!/usr/bin/python
import fileinput

started=False
prefix=""
for line in fileinput.input():
	line=line.strip("\n")
	
	#ignore unrelated and juniper
	if line.find("Compare") < 0 and line.find("router:bd03f2.csb1") < 0:
		if not started:
			prefix=line
			started=True
		else:
			if line.find(prefix) < 0:
				r1=prefix.rfind(':')
				r2=line.rfind(':')
				rfixed1=prefix.rfind(')')
				rfixed2=line.rfind(')')
				if rfixed1<r1 and rfixed2 < r2:
					subprefix=prefix[1:r1]
					subline=line[1:r2]
					if subprefix==subline:
						print "m" + subprefix
						started=False
					else:
						print prefix
						prefix = line
				else:
					print prefix
					prefix = line

print prefix
			
