package processor;
import org.bytedeco.javacpp.opencv_core.IplImage;
import api.*;

public class ConfigManager {
	
	
	 private IplImage originImage; 
	 
	
	 public static void main(String[] args){
	
	 ConfigManager manager = new ConfigManager(); 
		 
	 // TODO ::  Read File image (Class FileImageReader)
	   
	 String filePath ="";
	 FileImageReader imageReader = new FileImageReader();
	 imageReader.loadImage(filePath);
	 manager.originImage = imageReader.getOriginImage();
		 
		 
	 // TODO ::	 Process Image (Class ImageProcessor)
	
		 
	 // TODO ::	 Segment Image based on Configuration file(Class ConfiguredFiledMatcher)
	
		 
	 // TODO ::  Get content from fields (Class ContentProcessor)
	
		 
	 // TODO ::  Call Tesseract    (Class Tesseract)
		 
	 
	 // TODO ::  Write string to DB  (Class DBWriter)
		 
		 
		 
		 
		 
	 }
	 
	
}
