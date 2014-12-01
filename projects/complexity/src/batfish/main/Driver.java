package batfish.main;

import java.io.File;
import java.util.Map;

public class Driver {

	public static void main(String []args) throws Exception{
		Batfish b=new Batfish();
		Map<File, String> configs = b.readConfigurationFiles("./");
		Map<String, Integer> complexity = b.parseVendorConfigurations(configs);
		for(String k: complexity.keySet()){
			System.out.println(k+":"+complexity.get(k));
		}
	}
}
