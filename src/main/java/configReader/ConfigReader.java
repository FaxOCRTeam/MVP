package configReader;

import java.io.InputStream;
import java.util.List;

import configReader.model.ConfigField;

public interface ConfigReader {
	List<ConfigField> loadingConfiguration(InputStream stream);
}
