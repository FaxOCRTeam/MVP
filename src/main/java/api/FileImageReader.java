package api;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class FileImageReader {
	private IplImage originImage;
	private IplImage grayImage;
	private IplImage binaryImage;
	
	public IplImage getOriginImage() {
		return originImage;
	}

	public IplImage getGrayImage() {
		return grayImage;
	}

	public IplImage getBinaryImage() {
		return binaryImage;
	}

	
	public void loadImage(String fileFullPath){
		
		//Load to Color
		 originImage = cvLoadImage(fileFullPath, CV_LOAD_IMAGE_COLOR);
		 cvShowImage("origin", originImage);
		 
		//Covert to Gray
		 grayImage = cvCreateImage(cvGetSize(originImage), IPL_DEPTH_8U, 1);
         cvCvtColor(originImage, grayImage, CV_BGR2GRAY);
         cvShowImage("gray", grayImage);
         
        //Covert to Binary 
     	 binaryImage = cvCreateImage(cvGetSize(grayImage), IPL_DEPTH_8U, 1);
		 cvThreshold(grayImage, binaryImage, 100, 255, CV_THRESH_BINARY);
	     cvShowImage("binary", binaryImage);
	}
}
