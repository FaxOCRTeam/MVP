package processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import api.ConfigReader;
import dataModel.ConfigField;

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

			String table = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					table = line.substring(1, line.length() - 2);
					continue;
				}
				if (null == table)
					continue;

				if (line.startsWith("*"))
					continue;

				String[] split = line.split(":", 2);
				String fieldName = split[0].trim();
				String[] numberStr = split[1].split(",");
				int[] number = new int[4];
				for (int i = 0; i < 4; i++) {
					number[i] = Integer.parseInt(numberStr[i].trim());
				}
				ConfigField field = new ConfigField(new int[] { number[0], number[1] }, //
						number[2], number[3], fieldName, table);
				
				result.add(field);

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
