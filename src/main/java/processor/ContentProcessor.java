package processor;

import java.util.List;

import net.sourceforge.tess4j.TesseractException;
import api.ImageRecognizer;
import api.TesseractImageRecognizer;
import dataModel.Field;
import api.ImageRecognizer.*;

public class ContentProcessor {
	
	public List<Field> process(List<Field> fields) throws TesseractException{
		ImageRecognizer imageRecognizer = new TesseractImageRecognizer();
		for(Field f:fields){
			String filedContent = imageRecognizer.getString(f.getImage());
			f.setContent(filedContent);
		}
		return fields;
	}
}
