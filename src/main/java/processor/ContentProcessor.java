package processor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.TesseractException;
import api.ImageRecognizer;
import api.TesseractImageRecognizer;
import dataModel.Field;
import api.ImageRecognizer.*;

public class ContentProcessor {
	
	public List<Field> process(List<Field> fields) throws TesseractException{
		ImageRecognizer imageRecognizer = new TesseractImageRecognizer();
		for(Field f:fields){
			System.out.println(f.getField());
//			if(f.getField().equals("Collection_Date")){
//				try {
//				    // retrieve image
//				    BufferedImage bi = f.getImage();
//				    File outputfile = new File("saved.jpg");
//				    ImageIO.write(bi, "jpg", outputfile);
//				} catch (IOException e) {
//				}
//			}
			String filedContent = imageRecognizer.getString(f.getImage());
			f.setContent(filedContent);
		}
		return fields;
	}
}
