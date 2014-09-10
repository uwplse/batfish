#!/usr/bin/python
import fileinput
import sys

filename=sys.argv[1]
f = open(filename,'r')

line = f.readline()

while line:
	line = line.strip("\n")
	if line.find("_bgpProcess") >= 0 and line.find("_activatedNeighbors") >= 0:
		ipl = line.rfind(':') + 1
		ip = line[ipl:]
		routerl = line.find(':') + 1
		routerr = line.find('_') 
		router = line[routerl:routerr]
		op = line[0]
		if op == "+":
			op = "add"
		elif op == "-":
			op = "remove"
		else:
			op = "WRONG op"
		line = f.readline()
		while line and line.find("_bgpProcess") >= 0:
			line = f.readline()
			continue
		print router+": " + op + " bgp peergroup: " +ip  	
		continue

	elif line.find("_bgpProcess") >= 0:
		print line
		line = f.readline()
		continue

	elif line.find("_extendedAccessLists") >= 0:
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel: namer]
		op = "modify"
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router+": " + op + " extendedAccessLists: " +name
		continue

	elif line.find("_interfaces") >= 0:
		namerr = line.find(')')
		subline = line[:namerr+1]
		if line.find("_active") >= 0:
			routerl = line.find(':') + 1
			routerr = line.find('_')
			router = line[routerl:routerr]
			namel = line.find('(') + 1
			namer = line.find(')') - 1
			name = line[namel:namer+1]
			oril = line.rfind(':') + 1
			ori = line[oril:]
			op = ""
			if ori == "true":
				op = "disable"
			elif ori == "false":
				op = "enable"
			else:
				op = "WRONG op!!"
				
			tag = line[routerl:namer]
			line = f.readline()
			while line and line.find(tag) >= 0:
				line = f.readline()
				continue;
			print router + ": " + op + " interface: " + name
			continue
		elif line == subline:
			op = line[0]
			if op != "-":
				print "ERR line: " + line
				break

			op = "delete"
			routerl = line.find(':') + 1
			routerr = line.find('_')
			router = line[routerl:routerr]
			namel = line.find('(') + 1
			namer = line.find(')') - 1
			name = line[namel:namer+1]
			tag = line[routerl:namer]
			line = f.readline()
			while line and line.find(tag) >= 0:
				line = f.readline()
				continue;
			print router + ": " + op + " interface: " + name
			continue
		else:
			print line
			line = f.readline()
			continue
			

		
	elif line.find("_prefixLists") >= 0:
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		op = "modify"
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel:namer]
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router+": " + op + " prefislists: " +name
		continue

	elif line.find("_ospfProcess._defaultInformationMetric") >= 0:
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		op = "modify"
		line = f.readline()
		while line and line.find("_ospfProcess._defaultInformationMetric") >= 0 and line.find(router) >= 0:
			line = f.readline()
			continue;
		print router+": " + op + " ospfProcess: defaultInformationMetric" 
		continue

	elif line.find("_routeMaps") >= 0:
		rr = line.rfind(')')
		subline = line[:rr+1]
		if line.find("_clauses") >= 0 and line.find("_localPreference"):
			routerl = line.find(':') + 1
			routerr = line.find('_')
			router = line[routerl:routerr]
			op = "modify"
			namel = line.find('(') + 1
			namer = line.find(')') - 1
			name = line[namel:namer+1]
			tag = line[routerl:namer+1]
			line = f.readline()
			while line and line.find(tag) >= 0:
				line = f.readline()
				continue;
			print router+": " + op + " routemaps localpreference: " + name 
			continue

		elif line.find("_clauses") >= 0 and line.find("_metric"):
			routerl = line.find(':') + 1
			routerr = line.find('_')
			router = line[routerl:routerr]
			op = "modify"
			namel = line.find('(') + 1
			namer = line.find(')') - 1
			name = line[namel:namer+1]
			tag = line[routerl:namer+1]
			line = f.readline()
			while line and line.find(tag) >= 0:
				line = f.readline()
				continue;
			print router+": " + op + " routemaps metric: " + name 
			continue
		elif subline == line:
			op = line[0]
			if op == "+":
				op = "add"
			elif op == "-":
				op = "remove"
			else:
				op = "WRONG op"
			routerl = line.find(':') + 1
			routerr = line.find('_')
			router = line[routerl:routerr]
			namel = line.find('(') + 1
			namer = line.find(')') - 1
			name = line[namel:namer+1]
			tag = line[routerr:namer]
			line = f.readline()
			while line and line.find(tag) >= 0:
				line = f.readline()
				continue;
			print router + ": " + op + " routeMaps: " + name
			continue
		

	elif line.find("_policyStatements") >= 0: 
		op = line[0]
		if op == "+":
			op = "add"
		elif op == "-":
			op = "remove"
		else:
			op = "WRONG op"
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel:namer+1]
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router + ": " + op + " policyStatements: " + name
		continue

	elif line.find("_routeFilters") >= 0: 
		op = line[0]
		if op == "+":
			op = "add"
		elif op == "-":
			op = "remove"
		else:
			op = "WRONG op"
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel:namer+1]
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router + ": " + op + " routeFilters: " + name
		continue


	elif line.find("_staticRoutes") >= 0: 
		op = line[0]
		if op == "+":
			op = "add"
		elif op == "-":
			op = "remove"
		else:
			op = "WRONG op"
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel:namer+1]
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router + ": " + op + " staticRoutes: " + name
		continue

	elif line.find("_standardAccessLists") >= 0: 
		op = line[0]
		if op == "+":
			op = "add"
		elif op == "-":
			op = "remove"
		else:
			op = "WRONG op"
		routerl = line.find(':') + 1
		routerr = line.find('_')
		router = line[routerl:routerr]
		namel = line.find('(') + 1
		namer = line.find(')') - 1
		name = line[namel:namer+1]
		tag = line[routerl:namer]
		line = f.readline()
		while line and line.find(tag) >= 0:
			line = f.readline()
			continue;
		print router + ": " + op + " standardAccessLists: " + name
		continue

	else:
		print line
		line = f.readline()
