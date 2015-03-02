package processor;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import net.sourceforge.tess4j.TesseractException;
import api.ImageRecognizer;
import api.TesseractImageRecognizer;
import dataModel.Field;
import api.ImageRecognizer.*;

public class ContentProcessor {
	
	public List<Field> process(List<Field> fields) throws TesseractException{
		ImageRecognizer imageRecognizer = new TesseractImageRecognizer();
		for(Field f:fields){
			ArrayList<ArrayList<IplImage>> imageList = f.getimageList();
		//	System.out.println(f.getField());
			String filedContent = "";
			for(ArrayList<IplImage> lineImageList:imageList){
				for(IplImage wordimage :lineImageList){
					filedContent += imageRecognizer.getString(wordimage.getBufferedImage())+" ";
				}
			}
			filedContent.trim();
		//	String filedContent = imageRecognizer.getString(f.getImage());
			f.setContent(filedContent);
		}
		return fields;
	}
}
