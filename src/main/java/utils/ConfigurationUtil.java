package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class ConfigurationUtil {
	static final String defaultFile = "config.properties";
	static final File propertiesFile = new File(defaultFile);
	static Properties properties = null;

	static {
		properties = new Properties();
		if (propertiesFile.exists()) {
			try {
				properties.load(new InputStreamReader(new FileInputStream(propertiesFile), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static void update(String key, String value) {
		properties.setProperty(key, value);
		try {
			properties.store(new PrintWriter(propertiesFile), "autoSave");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
