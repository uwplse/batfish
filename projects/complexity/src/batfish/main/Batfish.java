package batfish.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import batfish.cisco.grammar.controlplane.CiscoControlPlaneComplexity;
import batfish.grammar.cisco.CiscoGrammar;
import batfish.grammar.cisco.CiscoGrammarCommonLexer;

public class Batfish {

	private String readFile(File file) throws Exception {
		String text = null;
		try {
			text = FileUtils.readFileToString(file);
		} catch (IOException e) {
			throw new Exception("Failed to read file: " + file.toString(), e);
		}
		return text;
	}

	public Map<File, String> readConfigurationFiles(String testRigPath)
			throws Exception {
		System.out.print("\n*** READING CONFIGURATION FILES ***\n");

		Map<File, String> configurationData = new TreeMap<File, String>();
		File configsPath = new File(new File(testRigPath), "configs");
		File[] configFilePaths = configsPath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		});
		if (configFilePaths == null) {
			throw new Exception("Error reading test rig configs directory");
		}
		for (File file : configFilePaths) {
			System.out.print("Reading: \"" + file.toString() + "\"\n");
			String fileText = readFile(file.getAbsoluteFile());
			configurationData.put(file, fileText);
		}
		return configurationData;
	}

	public Map<String, Integer> parseVendorConfigurations(
			Map<File, String> configurationData) {
		System.out.println("\n*** PARSING VENDOR CONFIGURATION FILES ***\n");
		Map<String, Integer> vendorConfigurations = new TreeMap<String, Integer>();
		for (File currentFile : configurationData.keySet()) {
			String fileText = configurationData.get(currentFile);
			String currentPath = currentFile.getAbsolutePath();

			CiscoGrammarCommonLexer lexer;
			CiscoGrammar parser;
			ANTLRInputStream inputStream = new ANTLRInputStream(fileText);
			lexer = new CiscoGrammarCommonLexer(inputStream);
			CommonTokenStream tokens = new CommonTokenStream((TokenSource) lexer);
			parser = new CiscoGrammar((TokenStream) tokens);
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
			CiscoControlPlaneComplexity extractor = new CiscoControlPlaneComplexity();
			
			if (fileText.charAt(0) == '!') {
				ParserRuleContext tree = parser.cisco_configuration();
				ParseTreeWalker walker = new ParseTreeWalker();
				walker.walk(extractor, tree);
				vendorConfigurations.put(currentFile.getName(), extractor.getComplexit());
			} else {
				System.out.print("Parsing: \"" + currentPath + "\" ERROR\n");
			}

		}
		return vendorConfigurations;
	}
}
