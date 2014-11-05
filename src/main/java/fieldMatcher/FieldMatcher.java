package fieldMatcher;

import java.awt.Image;
import java.util.List;

import configReader.model.ConfigField;
import fieldMatcher.model.Field;

public interface FieldMatcher {
	List<Field> imageSegmentation(Image workingImage, List<ConfigField> configField);
}
