package processor;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

import dataModel.Field;

public class ULremove {
	public static int count = 1;
	
	public static IplImage underlineRemove(IplImage image) {
		CvMat imgMat = image.asCvMat();
		int imgCols = imgMat.cols();
		int imgRows = imgMat.rows();
		int[] shadow = new int[imgRows];
		for (int i = 0; i < imgRows; i++) {
			for (int j = 0; j < imgCols; j++) {
				if (imgMat.get(i, j) == 0) {
					shadow[i]++;
				}
			}
		}
		List<Integer[]> underlineList = new ArrayList<Integer[]>();
		for (int i = 0; i < imgRows; i++) {
			if (shadow[i] > imgCols * 0.4) {
				int count = 0;
				int blank = 0;
				int startpoint = 0;
				for(int j =0 ; j < imgCols;j++){
					if(imgMat.get(i,j)==0){
						if(count==0){
							startpoint =j;
						}
						count++;
						count +=blank;
						blank = 0;
						if(j==imgCols -1&&count>imgCols*0.35){
							Integer[] insertInt = {startpoint, count};
							underlineList.add(insertInt);
						}
					}
					else if(count>0&&blank<3){
						blank++;
					}
					else if(count>0){
						if(count>imgCols *0.35){
							Integer[] insertInt = {startpoint, count};
							underlineList.add(insertInt);
						}
						blank = 0;
						count = 0;
					}
				}
				
				for(Integer[] j :underlineList){
					for(int k = 0;k<j[1];k++){
						imgMat.put(i, k+j[0], 255);
					}
				}
			}
		}
	//	IplImage nImage = imgMat.asIplImage();
		org.bytedeco.javacpp.opencv_highgui.cvSaveImage("./src/image"+count+".jpg",
				image);
		count++;
		return image;
	}
}