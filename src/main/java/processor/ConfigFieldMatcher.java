package processor;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvRect;

import org.bytedeco.javacv.CanvasFrame;

import dataModel.ConfigField;
import dataModel.Field;
import api.FieldMatcher;

public class ConfigFieldMatcher implements FieldMatcher {

	@Override
	public List<Field> imageSegmentation(IplImage workingImage,
			List<ConfigField> configField) {
		// TODO Auto-generated method stub
	//	List<Field> imageResult = new ArrayList<Field>();
		//IplImage image = cvCloneImage(workingImage);
		IplImage ori = cvCreateImage(
				cvGetSize(workingImage),
				workingImage.depth(), workingImage.nChannels());
		
		ConfigField singleCF;
		List<Field> imageField = new ArrayList<Field>();
		// cut the image
		for (int i = 0; i < configField.size(); i++) {
			singleCF = configField.get(i);
			int[] point =singleCF.getStartingPoint();
			int startX = point[0];
			int startY = point[1];
			int endX = point[0]+singleCF.getWidth();
			int endY = point[1]+singleCF.getHeight();
			IplImage image = cvCloneImage(workingImage);
			IplImage blobImage;
			cvCopy(image, ori);
			
			cvSetImageROI(ori, cvRect(startX, startY, singleCF.getWidth() , singleCF.getHeight() ));
			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
					ori.nChannels());
			cvCopy(ori, blobImage);

			cvResetImageROI(ori);
			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(singleCF.getField()+".jpg",blobImage);
			Field ir = new Field();
			ir.setConfig(singleCF);
			ir.setField(singleCF.getField());
			ir.setImage(blobImage.getBufferedImage());
			imageField.add(ir);
		}



		return imageField;
	}

}
