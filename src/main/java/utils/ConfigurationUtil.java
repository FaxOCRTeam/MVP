package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurationUtil {
	private static class PropertiesFile {
		boolean initalized;
		Properties properties;
		File file;

		public PropertiesFile(File file) {
			super();
			this.file = file;
			this.properties = new Properties();
			if (file.exists()) {
				try {
					properties.load(new InputStreamReader(new FileInputStream(file), "utf-8"));
					initalized = true;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public boolean isInitalized() {
			return initalized;
		}

		public String get(String key) {
			return properties.getProperty(key);
		}

		public void update(String key, String value) {
			properties.setProperty(key, value);
			try {
				properties.store(new PrintWriter(file), "autoSave");
				initalized = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public List<String> keys() {
			List<String> result = new ArrayList<String>();
			Enumeration<Object> keys = properties.keys();
			while (keys.hasMoreElements()) {
				result.add((String) keys.nextElement());
			}
			return result;
		}
	}

	static Map<String, PropertiesFile> propertiesFileMap = new HashMap<String, ConfigurationUtil.PropertiesFile>();

	static final String defaultFile = "config.properties";
	static {
		PropertiesFile defaultConfig = new PropertiesFile(new File(defaultFile));
		propertiesFileMap.put(null, defaultConfig);
		final String fileLoaderPath = "src/main/resources/configs/file_loader_config.properties"; 
		final String dbConfigPath = "src/main/resources/daogenerator_config.properties";
		final String emailWatchDogConfigPath = "src/main/resources/configs/email_watchdog_configs.properties";
		
		if (!defaultConfig.isInitalized()) {
			defaultConfig.update("file_loader_config", fileLoaderPath);
			defaultConfig.update("dao_config", dbConfigPath);
			defaultConfig.update("email_watchdog_config", emailWatchDogConfigPath);
		}
		if (StringUtils.isEmpty(defaultConfig.get("file_loader_config")))
			defaultConfig.update("file_loader_config", fileLoaderPath);
		open(get(null, "file_loader_config"), false, "fileLoader");

		if (StringUtils.isEmpty(defaultConfig.get("dao_config")))
			defaultConfig.update("dao_config", dbConfigPath);
		open(get(null, "dao_config"), false, "dao");
		
		if (StringUtils.isEmpty(defaultConfig.get("email_watchdog_config")))
			defaultConfig.update("email_watchdog_config", emailWatchDogConfigPath);
		open(get(null, "email_watchdog_config"), false, "watchdog");
	}

	// public static String get(String columnKey) {
	// return get(null, columnKey);
	// }

	public static String get(String fileKey, String columnKey) {
		return propertiesFileMap.get(fileKey).get(columnKey);
	}

	// public static void update(String columnKey, String value) {
	// update(null, columnKey, value);
	// }

	public static List<String> keys(String fileKey) {
		return propertiesFileMap.get(fileKey).keys();
	}

	public static void update(String fileKey, String columnKey, String value) {
		propertiesFileMap.get(fileKey).update(columnKey, value);
	}

	public static void open(String path, boolean internal, String key) {
		File f = null;
		if (internal) {
			URL resource = ConfigurationUtil.class.getClassLoader().getResource(path);
			f = new File(resource.getFile());
		} else {
			f = new File(path);
		}
		System.out.println(f.getAbsolutePath());
		propertiesFileMap.put(key, new PropertiesFile(f));
	}

	public static boolean isOpened(String key) {
		return propertiesFileMap.containsKey(key);
	}
}
