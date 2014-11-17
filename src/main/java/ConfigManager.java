import java.io.InputStream;
import java.util.List;

import dataModel.ConfigField;
import processor.FileConfigReader;
import api.ConfigReader;


public class ConfigManager {
	public static void main(String[] args) {
		ConfigReader reader = new FileConfigReader();
		InputStream configStream = ConfigManager.class.getClassLoader().getResourceAsStream("ImageSegment.config");
		List<ConfigField> configList = reader.loadingConfiguration(configStream);
		
//		System.out.println(configList.size());
	}
}
