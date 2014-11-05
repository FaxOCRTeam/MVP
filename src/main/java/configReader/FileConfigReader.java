package configReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import configReader.model.ConfigField;

public class FileConfigReader implements ConfigReader {

	public List<ConfigField> loadingConfiguration(InputStream stream) {
		List<ConfigField> result = new ArrayList<ConfigField>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				// TODO: add detail parsing in here
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
