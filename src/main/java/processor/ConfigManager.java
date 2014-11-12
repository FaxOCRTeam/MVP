package processor;


import java.io.InputStream;
import java.util.List;

import net.sourceforge.tess4j.TesseractException;

import org.bytedeco.javacpp.opencv_core.IplImage;

import dataModel.Field;
import api.*;

public class ConfigManager {
	
	
	 private IplImage originImage; 
	 
	
	 public static void main(String[] args){
	
	 ConfigManager manager = new ConfigManager(); 
		 
	 // TODO ::  Read File image (Class FileImageReader)
	   
//	 String filePath ="";
//	 FileImageReader imageReader = new FileImageReader();
//	 imageReader.loadImage(filePath);
//	 manager.originImage = imageReader.getOriginImage();
	 
	 
	 // TODO ::	 Process Image (Class ImageProcessor)
	 ImageProcessor imageProcessor = new ImageProcessor();
	 InputStream configStream = ConfigManager.class.getClassLoader().getResourceAsStream("ImageSegment.config");
	 List<Field> imageField = imageProcessor.process(configStream, "Carol_Cheng .jpg");
	// imageProcessor.process(configStream, "F:\\ScanImage.jpg");
	 
		 
	 // TODO ::	 Segment Image based on Configuration file(Class ConfiguredFiledMatcher)
	
		 
	 // TODO ::  Get content from fields (Class ContentProcessor)
	 ContentProcessor contentProcessor = new ContentProcessor();
	 try {
		contentProcessor.process(imageField);
	} catch (TesseractException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 // TODO ::  Call Tesseract    (Class Tesseract)
	 
	 
	 // TODO ::  Write string to DB  (Class DBWriter)
	 DBWriterImpl dbWriter = new DBWriterImpl();
	 dbWriter.writeToDB(imageField);
		 
		 
		 
		 
	 }
	 
	
}
