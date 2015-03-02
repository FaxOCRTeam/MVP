package processor;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;

import services.dao.Fax2EMR.model.Fax2EMRPatientInformation;
import dataModel.ConfigField;
import api.ConfigReader;
import api.FileImageReader;

public class test {
	
	public static void main(String[] args) throws IOException{
		//image skews correction
		BufferedImage img =  ImageIO.read(new File("p-00003.tif"));
		IplImage origImg = IplImage.createFrom(img);
		final CanvasFrame canvas = new CanvasFrame("original");
		canvas.showImage(origImg);
		canvas.setSize((int)(img.getWidth()*0.4), (int)(img.getHeight()*0.4));
		ImagePreprocessor ipp = new ImagePreprocessor(origImg);
		IplImage processedImg = ipp.deskew(origImg);
		final CanvasFrame canvas2 = new CanvasFrame("processed");
		canvas2.showImage(processedImg);
		canvas2.setSize((int)(img.getWidth()*0.4), (int)(img.getHeight()*0.4));
		
		// field segmentation skipped 
		String dir = "./input/";
		IplImage originImage = cvLoadImage(dir+"test.png", CV_LOAD_IMAGE_GRAYSCALE);
		
		// image preprocessing
		IplImage binaryImage = cvCreateImage(cvGetSize(originImage),
					IPL_DEPTH_8U, 1);
		cvThreshold(originImage, binaryImage, 100, 255, CV_THRESH_BINARY);
		cvErode(binaryImage, binaryImage, null, 1);
		cvDilate(binaryImage, binaryImage, null, 1);
		
		// underline removal
		IplImage ulremovedImg = ULremover.underlineRemove(binaryImage)
	;		
		org.bytedeco.javacpp.opencv_highgui.cvSaveImage("output/ulRemovedTest.jpg", ulremovedImg);
		// word segmentation
		SpaceRemover.spaceRemove(ulremovedImg);
	}
	
//	public String Last_Name ="abc";
//	public static void main(String[] args) {
//		Fax2EMRPatientInformation t = new Fax2EMRPatientInformation();
//		Class cls = t.getClass();
//		try {
//			System.out.println(cls.getField("Last_Name"));
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
		
//	}
}
