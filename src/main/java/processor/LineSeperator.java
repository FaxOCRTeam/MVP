package processor;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

public class LineSeperator {
	public static void main(String[] args){
		LineSeperator ls = new LineSeperator();
		String dir = "./src/input/";
		System.out.print(dir);
		//String dir = "./src/input1/";
		for (int i=1; i<2; i++){
			String file = dir + "image" + i + ".jpg";
			//String file = dir + i + ".png";
			IplImage originImage = cvLoadImage(file, CV_LOAD_IMAGE_GRAYSCALE);
//			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(
//					"./src/originImage.jpg", originImage);
//			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(
//					"./src/grayImage.jpg", grayImage);
			IplImage binaryImage = cvCreateImage(cvGetSize(originImage),
					IPL_DEPTH_8U, 1);
			cvThreshold(originImage, binaryImage, 100, 255, CV_THRESH_BINARY);
//			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(
//					"./src/binaryImage.jpg", binaryImage);
			cvErode(binaryImage, binaryImage, null, 1);
			cvDilate(binaryImage, binaryImage, null, 1);
			binaryImage = verticalbarremove(binaryImage);
			lineSperate(binaryImage);
		}
	}
	public static List<IplImage> lineSperate(IplImage image){
		image = verticalbarremove(image);
		CvMat imgMat = image.asCvMat();
		int imgCols = imgMat.cols();
		int imgRows = imgMat.rows();
		int[] areaOfLine = new int[imgRows];
		int isInLine = 0;
		IplImage ori = cvCreateImage(
				cvGetSize(image),
				image.depth(), image.nChannels());
		ArrayList<Spaces> spaceAndWidth = new ArrayList<Spaces>();
		ArrayList<Integer> lineLen = new ArrayList<Integer>();
	//	org.bytedeco.javacpp.opencv_highgui.cvSaveImage("./test.jpg",image);
		int startOfSpace = 0, widthOfSpace = 0;
		int currentCharStart = 0, currentCharWidth = 0;
		for (int i = 0; i < imgRows; i++) {
			for (int j = 0; j < imgCols; j++) {
			//	System.out.println(j+" "+i);
				if (imgMat.get(i, j) == 0) {
					areaOfLine[i]++;
				}
			}
			if(isInLine == 0){
				if (areaOfLine[i] != 0 ){
						isInLine = 1;
						currentCharStart = i;
						widthOfSpace = i - startOfSpace;
						if(widthOfSpace != 0)
							spaceAndWidth.add(new Spaces(startOfSpace,widthOfSpace));
				}
				else if(i == imgRows-1){
			//		System.out.println(areaOfLine[i]);
					widthOfSpace = i - startOfSpace;
					if(widthOfSpace != 0)
						spaceAndWidth.add(new Spaces(startOfSpace,widthOfSpace));
				}
			}
			else{
				if (areaOfLine[i] == 0 ){
					isInLine = 0;
					startOfSpace = i;
					currentCharWidth = i - currentCharStart;
					if(currentCharWidth != 0)
						lineLen.add(currentCharWidth);
				}
				else if (i == imgCols-1){
					//System.out.println(areaOfWord[i]);
					currentCharWidth = i - currentCharStart;
					if(currentCharWidth != 0)
						lineLen.add(currentCharWidth);
				}
			}
		}
		Collections.sort(spaceAndWidth,Collections.reverseOrder());
		Collections.sort(lineLen);
		//System.out.println(count);
		int charMedian = lineLen.get(lineLen.size()/2);
		//System.out.println(charMedian);
		Iterator<Spaces> itr = spaceAndWidth.iterator();
		ArrayList<Spaces> spaceBetWords = new ArrayList<Spaces>();
		while (itr.hasNext()){
			Spaces spc = itr.next();
			if(spc.getWidth() < charMedian * 0.5 )
				break;
			spaceBetWords.add(new Spaces(spc.getWidth(),spc.getStart()));
		}
//		for(int i = 0;i<spaceBetWords.size()-1;i++){///////////////////////////////////
//			Spaces n_spc = spaceBetWords.get(i+1);
//			Spaces c_spc = spaceBetWords.get(i);
//			c_spc.width = n_spc.start;
//			spaceBetWords.set(i, c_spc);
//		}
//		Spaces c_spc = spaceBetWords.get(spaceBetWords.size()-1);
//		c_spc.width = imgRows;
		spaceBetWords.add(new Spaces(Integer.MAX_VALUE,imgRows));
	//	Spaces spc = new Spaces(Integer.MAX_VALUE,imgRows);
		Collections.sort(spaceBetWords);
		Iterator<Spaces> itsp = spaceBetWords.iterator();
		int wordStart = 0;
		int pos = 1;
		ArrayList<IplImage> returnlist = new ArrayList<IplImage>();
		while(itsp.hasNext()){
			Spaces oneSpace = itsp.next();
			if(oneSpace.getWidth() == 0){
				continue;
			}
			IplImage clImage = cvCloneImage(image);
			IplImage blobImage;
			
			cvCopy(clImage, ori);			
			cvSetImageROI(ori, cvRect(0,wordStart,imgCols,oneSpace.getWidth() - wordStart));
			wordStart = oneSpace.getWidth() + oneSpace.getStart();
			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
					ori.nChannels());
			cvCopy(ori, blobImage);
			cvResetImageROI(ori);

			//File output = new File("./src/subimage_"+ pos +"of image"+count+".jpg");
			File dir = new File("./output/image" );
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			returnlist.add(blobImage);
			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(dir.toString()+"/"+ pos + ".jpg",blobImage);
			pos ++;
		}
		return returnlist;
	}
	
	public static IplImage verticalbarremove(IplImage image){
		CvMat imgMat = image.asCvMat();
		int imgCols = imgMat.cols();
		int imgRows = imgMat.rows();
		int[] shadow = new int[imgCols];
		for (int i = 0; i < imgCols; i++) {
			for (int j = 0; j < imgRows; j++) {
				if (imgMat.get(j,i) == 0) {
					shadow[i]++;
				}
			}
		}
		List<Integer[]> barList = new ArrayList<Integer[]>();
		for (int i = 0; i < imgCols; i++) {
			if (shadow[i] > imgRows * 0.8) {
				int count = 0;
				int blank = 0;
				int startpoint = 0;
				for(int j =0 ; j < imgRows;j++){
					if(imgMat.get(j,i)==0){
						if(count==0){
							startpoint =j;
						}
						count++;
						count +=blank;
						blank = 0;
						if(j==imgRows -1&&count>imgRows*0.7){
							Integer[] insertInt = {startpoint, count};
							barList.add(insertInt);
						}
					}
					else if(count>0&&blank<2){
						blank++;
					}
					else if(count>0){
						if(count>imgCols *0.7){
							Integer[] insertInt = {startpoint, count};
							barList.add(insertInt);
						}
						blank = 0;
						count = 0;
					}
				}
				
				for(Integer[] j :barList){
					for(int k = 0;k<j[1];k++){
						imgMat.put(k+j[0], i, 255);
					}
				}
			}
		}
		return image;
	}
}
