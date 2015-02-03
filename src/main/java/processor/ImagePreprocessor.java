package processor;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;

import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.vietocr.ImageHelper;

public class ImagePreprocessor {
	// Original IplImage from FileImageReader
	private IplImage oriIplImage;
	// Converted BufferedImage for doing deskew
	private BufferedImage bufferedImage;
	
	public ImagePreprocessor(IplImage image){
		oriIplImage = image;
	}
	
	public IplImage deskew(IplImage image){
		BufferedImage bImg = image.getBufferedImage(); 
		ImageDeskew deskew = new ImageDeskew(bImg);
		double angle = deskew.getSkewAngle();
		ImageHelper ih = new ImageHelper();
		BufferedImage rImg = ih.rotateImage(bImg, -angle);
		IplImage ret = IplImage.createFrom(rImg);
		return ret;
	}
	
	public IplImage erodeAndDilateImage(IplImage image){
		int ErodeCount = 1;
		int DilateCount = 1;
		int threshold = 150;
		
		IplImage rawImage = cvCloneImage(image);
		IplImage GrayImage = cvCreateImage(cvGetSize(rawImage), IPL_DEPTH_8U, 1);
		cvCvtColor(rawImage, GrayImage, CV_BGR2GRAY);
		
		IplImage BWImage = cvCreateImage(cvGetSize(GrayImage), IPL_DEPTH_8U, 1);
		cvThreshold(GrayImage, BWImage, 100, 255, CV_THRESH_BINARY);
		
		IplImage WorkingImage = cvCreateImage(cvGetSize(BWImage), IPL_DEPTH_8U, 1);

		cvErode(BWImage, WorkingImage, null, ErodeCount);
		cvDilate(WorkingImage, WorkingImage, null, DilateCount);

		cvDilate(WorkingImage, WorkingImage, null, DilateCount);
		cvErode(BWImage, WorkingImage, null, ErodeCount);
		return WorkingImage;
	}
/*	
	public static void main(String[] args) throws IOException{
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
	}
	*/
}
