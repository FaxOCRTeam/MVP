package api;

import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import dataModel.ConfigField;
import dataModel.Field;

public interface FieldMatcher {
	List<Field> imageSegmentation(IplImage workingImage, List<ConfigField> configField);
}
