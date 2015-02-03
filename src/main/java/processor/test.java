package processor;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;

import java.io.InputStream;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import services.dao.Fax2EMR.model.Fax2EMRPatientInformation;
import dataModel.ConfigField;
import api.ConfigReader;
import api.FileImageReader;

public class test {
	public String Last_Name ="abc";
	public static void main(String[] args) {
		Fax2EMRPatientInformation t = new Fax2EMRPatientInformation();
		Class cls = t.getClass();
		try {
			System.out.println(cls.getField("Last_Name"));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		FileImageReader imageReader = new FileImageReader();
//
//		imageReader.loadImage("Carol_Cheng .jpg");// Read image from ImageReader.
//		IplImage workingImage = imageReader.getBinaryImage();
//
//		ConfigReader configReader = new FileConfigReader();
//		InputStream configStream = ConfigManager.class.getClassLoader()
//				.getResourceAsStream("ImageSegment.config");
//		List<ConfigField> conFieldList = configReader
//				.loadingConfiguration(configStream);
//		ConfigField singleCF;
//
//		for (ConfigField cf : conFieldList) {
//			singleCF = cf;
//			int[] point =singleCF.getStartingPoint();
//			int startX = point[0];
//			int startY = point[1];
//			int endX = point[0]+singleCF.getWidth();
//			int endY = point[1]+singleCF.getHeight();
//			IplImage image = cvCloneImage(workingImage);
//			IplImage ori = cvCreateImage(cvGetSize(image), image.depth(),
//					image.nChannels());
//			IplImage blobImage;
//			cvCopy(image, ori);
//
//			cvSetImageROI(ori,
//					cvRect(startX, startY, singleCF.getWidth(), singleCF.getHeight()));
//
//			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
//					ori.nChannels());
//			cvCopy(ori, blobImage);
//			cvResetImageROI(ori);
//			cvShowImage("binary", blobImage);
//			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(singleCF.getField()+".jpg",blobImage);
//
//		}
		
	}
}
