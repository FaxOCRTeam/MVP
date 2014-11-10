package processor;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.CanvasFrame;

import dataModel.ConfigField;
import dataModel.Field;
import api.FieldMatcher;

public class ConfigFieldMatcher implements FieldMatcher {

	@Override
	public List<Field> imageSegmentation(IplImage workingImage,
			List<ConfigField> configField) {
		// TODO Auto-generated method stub
		List<Field> imageResult = new ArrayList<Field>();
		List<ConfigField> result = new ArrayList<ConfigField>();
		// result = FileConfigReader.loadingConfiguration(stream); //From
		// FileConfigReader.java;

	//	IplImage image = null; // From the ImageReader;
		IplImage ori = org.bytedeco.javacpp.opencv_core.cvCreateImage(
				org.bytedeco.javacpp.opencv_core.cvGetSize(workingImage),
				workingImage.depth(), workingImage.nChannels());
		ConfigField singleCF;
		List<Field> imageField = new ArrayList<Field>();
		// cut the image
		for (int i = 0; i < configField.size(); i++) {
			singleCF = configField.get(i);
			int[] point =singleCF.getStartingPoint();
			int startX = point[0];
			int startY = point[1];
			point =singleCF.getEndingPoint();
			int endX = point[0];
			int endY = point[1];
			org.bytedeco.javacpp.opencv_core.cvSetImageROI(ori, org.bytedeco.javacpp.opencv_core.cvRect(startX, startY, endX - startX, endY - startY));
			Field ir = new Field();
			ir.setConfig(singleCF);
			ir.setField(singleCF.getField());
			ir.setImage(ori.getBufferedImage());
			CanvasFrame canvas = new CanvasFrame("STring", 1);   // gamma=1
		    canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		    CvMat mat = ori.asCvMat();
		    canvas.setCanvasSize(mat.cols(), mat.rows());
		    canvas.showImage(ori);
			imageField.add(ir);
		}



		return imageField;
	}

}
