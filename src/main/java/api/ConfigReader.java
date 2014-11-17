package api;

import java.io.InputStream;
import java.util.List;

import dataModel.ConfigField;

public interface ConfigReader {
	List<ConfigField> loadingConfiguration(InputStream stream);
}
