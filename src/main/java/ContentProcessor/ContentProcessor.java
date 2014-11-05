package ContentProcessor;

import java.util.List;

import fieldMatcher.model.Field;
import ImageRecognizer.*;

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
