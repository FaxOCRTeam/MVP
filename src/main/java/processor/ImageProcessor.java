package processor;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import api.ConfigReader;
import api.FieldMatcher;
import dataModel.ConfigField;
import dataModel.Field;

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
