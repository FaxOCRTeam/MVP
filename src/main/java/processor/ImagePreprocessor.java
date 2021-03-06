package processor;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;




import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_core.cvSetZero;
import static org.bytedeco.javacpp.opencv_core.cvNot;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.IplConvKernel;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;

import com.recognition.software.jdeskew.ImageDeskew;

public class ImagePreprocessor {

	
	public IplImage deskew(IplImage image){
		BufferedImage bImg = image.getBufferedImage();
		IplImage ret = IplImage.create(image.width(),image.height(), IPL_DEPTH_8U, 1);
		cvSetZero(ret);
		cvNot(ret,ret);
		ImageDeskew deskew = new ImageDeskew(bImg);
		double angle = deskew.getSkewAngle();
		
		CvPoint2D32f my_center = new CvPoint2D32f();
		my_center.put((double)(image.width() / 2), (double)(image.height() / 2));
		int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
		CvScalar fillval = cvScalarAll(255);
		CvMat map_matrix = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(my_center, angle, 1, map_matrix);
		cvWarpAffine(image, ret, map_matrix, flags, fillval);
		
		return ret;
	}
	
	public BufferedImage deskew(BufferedImage bImg) {
		ImageDeskew deskew = new ImageDeskew(bImg);
		double angle = deskew.getSkewAngle();
		// Convert colored BufferedImage to gray scale
	    BufferedImage gimage = new BufferedImage(bImg.getWidth(), bImg.getHeight(),
	    		BufferedImage.TYPE_BYTE_GRAY);  
	    Graphics g = gimage.getGraphics();  
	    g.drawImage(bImg, 0, 0, null);  
	    g.dispose();
	    
		IplImage ret = IplImage.create(bImg.getWidth(),bImg.getHeight(), IPL_DEPTH_8U, 1);
		cvSetZero(ret);
		cvNot(ret,ret);
		
		IplImage ret2 = IplImage.create(bImg.getWidth(),bImg.getHeight(), IPL_DEPTH_8U, 1);
		ret2.copyFrom(gimage);
		
		CvPoint2D32f my_center = new CvPoint2D32f();
		my_center.put((double)(bImg.getWidth() / 2), (double)(bImg.getWidth() / 2));
		int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
		CvScalar fillval = cvScalarAll(255);
		CvMat map_matrix = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(my_center, angle, 1, map_matrix);
		cvWarpAffine(ret2, ret, map_matrix, flags, fillval);
		
		return ret.getBufferedImage();
	}
	
	public IplImage erodeAndDilateImage(IplImage image, int ErodeCount, int DilateCount){
		int threshold = 255;
		IplImage rawImage = cvCloneImage(image);
		IplImage GrayImage = cvCreateImage(cvGetSize(rawImage), IPL_DEPTH_8U, 1);
		if (rawImage.nChannels() != 1)
			cvCvtColor(rawImage, GrayImage, CV_BGR2GRAY);
		else
			GrayImage = rawImage;
		IplImage BWImage = cvCreateImage(cvGetSize(GrayImage), IPL_DEPTH_8U, 1);
		//cvThreshold(GrayImage, BWImage, 100, 255, CV_THRESH_BINARY);
		cvAdaptiveThreshold(GrayImage, BWImage, threshold, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 17,30);
		IplConvKernel mat= new IplConvKernel();
		mat = mat.create(7, 7, 3, 3, 0, null);
		IplImage WorkingImage = cvCreateImage(cvGetSize(BWImage), IPL_DEPTH_8U, 1);

//		cvErode(BWImage, WorkingImage, mat, ErodeCount);
//		cvDilate(WorkingImage, WorkingImage, mat, DilateCount);
//
		cvDilate(WorkingImage, WorkingImage, mat, DilateCount);
		cvErode(BWImage, WorkingImage, mat, ErodeCount);
		return WorkingImage;
	}

/*
	public static void main(String[] args) throws IOException{
		BufferedImage img =  ImageIO.read(new File("Freddie's Form.jpg"));
		IplImage origImg = IplImage.createFrom(img);
		final CanvasFrame canvas = new CanvasFrame("original");
		canvas.showImage(origImg);
		canvas.setSize((int)(img.getWidth()*0.4), (int)(img.getHeight()*0.4));
		ImagePreprocessor ipp = new ImagePreprocessor();
		BufferedImage processedImg = ipp.deskew(img);
		final CanvasFrame canvas2 = new CanvasFrame("processed");
		canvas2.showImage(processedImg);
		canvas2.setSize((int)(img.getWidth()*0.4), (int)(img.getHeight()*0.4));
	}
*/
}
