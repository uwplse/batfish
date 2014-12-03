all:
#	cp /scratch/batfish/projects/batfish/src/batfish/grammar/cisco/*.g4 src/
	cd src; java -jar ../lib/antlr-4.4-complete.jar CiscoGrammarCommonLexer.g4
	cd src; java -jar ../lib/antlr-4.4-complete.jar CiscoGrammar.g4
	cp src/*.java ../src/batfish/grammar/cisco/
