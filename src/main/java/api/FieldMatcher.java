package api;

import java.awt.Image;
import java.util.List;

import dataModel.ConfigField;
import dataModel.Field;

public interface FieldMatcher {
	List<Field> imageSegmentation(Image workingImage, List<ConfigField> configField);
}
