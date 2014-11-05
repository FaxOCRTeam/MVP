package processor;
import java.util.List;

import dataModel.Field;
import api.ImageRecognizer;
public class ContentProcessor {
	public List<Field> process(List<Field> fields){
		ImageRecognizer imageRecognizer = new TesseractImageRecognizer();
		for(Field f:fields){
			String filedContent = imageRecognizer.getString(f.getImage());
			f.setContent(filedContent);
		}
		return fields;
	}

	
	
}
