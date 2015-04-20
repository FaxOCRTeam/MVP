package processor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import api.ConfigReader;
import api.FieldMatcher;
import api.FileImageReader;
import dataModel.ConfigField;
import dataModel.Field;

public class ImageProcessor {
	public List<Field> process(List<ConfigField> config, String imagePath) {
		FileImageReader imageReader = new FileImageReader();
		
		imageReader.loadImage(imagePath);// Read image from ImageReader.
		IplImage workingImage = imageReader.getBinaryImage();
		
		FieldMatcher fieldMatcher = new ConfigFieldMatcher();// need initialization
		List<Field> imgFieldList = fieldMatcher.imageSegmentation(workingImage, config);
		
		for(Field f:imgFieldList){
			IplImage fieldImage = IplImage.createFrom(f.getImage());
			
			ArrayList<ArrayList<IplImage>> imageList = new ArrayList<ArrayList<IplImage>>();
			
			
			fieldImage = LineSeperator.verticalbarremove(fieldImage);
			ArrayList<IplImage> lineImageList = (ArrayList<IplImage>) LineSeperator.lineSperate(fieldImage);
			for(IplImage image:lineImageList){
				org.bytedeco.javacpp.opencv_highgui.cvSaveImage("./test3.jpg",image);
				image = ULremover.underlineRemove(image);
				org.bytedeco.javacpp.opencv_highgui.cvSaveImage("./test2.jpg",image);
				ArrayList<IplImage> wordImageList = SpaceRemover.spaceRemove(image);
				imageList.add(wordImageList);
			}
			f.setimageList(imageList);
		}
		return imgFieldList;
		
	}
	public List<Field> process(InputStream configStream, String imagePath){
		ConfigReader configReader = new FileConfigReader();// need initialization = new ConfigReader();
		List<ConfigField> conFieldList = configReader.loadingConfiguration(configStream);
		return process(conFieldList, imagePath);
	}
	
//	public static void main(String[] args){
//		IplImage originImage;
//		String path = "unnamed.jpg";
//		originImage = cvLoadImage(path, CV_LOAD_IMAGE_COLOR);
//	}
}
