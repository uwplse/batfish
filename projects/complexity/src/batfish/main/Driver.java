package batfish.main;

import java.io.File;
import java.util.Map;

public class Driver {

	public static void main(String []args) throws Exception{
		Batfish b=new Batfish();
		String path;
		//path = args[0];
		path = "./";
		Map<File, String> configs = b.readConfigurationFiles(path);
		b.parseVendorConfigurations(configs);
		
		b.outputComplexity(path);
	}
	
	
}
