package imageProcessor;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import configReader.ConfigReader;
import configReader.model.ConfigField;
import fieldMatcher.FieldMatcher;
import fieldMatcher.model.Field;

public class ImageProcessor {
	public List<Field> process(InputStream configStream, InputStream imageStream){
		ConfigReader configReader = null;// need initialization = new ConfigReader();
		List<ConfigField> conFieldList = configReader.loadingConfiguration(configStream);
		
		Image workingImage = null;// Read image from ImageReader.
		
		FieldMatcher fieldMatcher = null;// need initialization
		List<Field> imgFieldList = fieldMatcher.imageSegmentation(workingImage, conFieldList);
		return imgFieldList;
	}
}
