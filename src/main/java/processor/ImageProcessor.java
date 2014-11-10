package processor;

import java.io.InputStream;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import api.ConfigReader;
import api.FieldMatcher;
import api.FileImageReader;
import dataModel.ConfigField;
import dataModel.Field;

public class ImageProcessor {
	public List<Field> process(InputStream configStream, InputStream imageStream){
		ConfigReader configReader = new FileConfigReader();// need initialization = new ConfigReader();
		List<ConfigField> conFieldList = configReader.loadingConfiguration(configStream);
		FileImageReader imageReader = new FileImageReader();
		
		imageReader.loadImage(imageStream.toString());// Read image from ImageReader.
		IplImage workingImage = imageReader.getBinaryImage();
		
		FieldMatcher fieldMatcher = new ConfigFieldMatcher();// need initialization
		List<Field> imgFieldList = fieldMatcher.imageSegmentation(workingImage, conFieldList);
		return imgFieldList;
	}
}
