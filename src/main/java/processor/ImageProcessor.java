package processor;

import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import java.io.InputStream;
import api.ConfigReader;
import api.FieldMatcher;
import api.FileImageReader;
import dataModel.ConfigField;
import dataModel.Field;

public class ImageProcessor {
	public List<Field> process(InputStream configStream, String imagePath){
		ConfigReader configReader = new FileConfigReader();// need initialization = new ConfigReader();
		List<ConfigField> conFieldList = configReader.loadingConfiguration(configStream);
		FileImageReader imageReader = new FileImageReader();
		
		imageReader.loadImage(imagePath);// Read image from ImageReader.
		IplImage workingImage = imageReader.getBinaryImage();
		
		FieldMatcher fieldMatcher = new ConfigFieldMatcher();// need initialization
		List<Field> imgFieldList = fieldMatcher.imageSegmentation(workingImage, conFieldList);
		
		return imgFieldList;
	}
	
//	public static void main(String[] args){
//		IplImage originImage;
//		String path = "unnamed.jpg";
//		originImage = cvLoadImage(path, CV_LOAD_IMAGE_COLOR);
//	}
}
