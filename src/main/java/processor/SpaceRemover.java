package processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
public class SpaceRemover {
	public static void main(String[] args) {
		String dir = "./src/input/";
//		System.out.print(dir);
		//String dir = "./src/input1/";
		for (int i=1; i<16; i++){
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
			spaceRemove(binaryImage);
		}
		  
	}
	public static int abc = 0;
	public static double SPACE_IN_WORD_THRE = 0.55;
	
	public static ArrayList<IplImage> spaceRemove(IplImage image) {
		CvMat imgMat = image.asCvMat();
		int imgCols = imgMat.cols();
		int imgRows = imgMat.rows();
		int[] areaOfWord = new int[imgCols];
		int isInWord = 0;
		IplImage ori = cvCreateImage(
				cvGetSize(image),
				image.depth(), image.nChannels());
		
		ArrayList<Spaces> spaceAndWidth = new ArrayList<Spaces>();
		ArrayList<Integer> wordLen = new ArrayList<Integer>();
		ArrayList<IplImage> returnlist = new ArrayList<IplImage>();

	//	org.bytedeco.javacpp.opencv_highgui.cvSaveImage("./test1.jpg",image);
		int startOfSpace = 0, widthOfSpace = 0;
		int currentCharStart = 0, currentCharWidth = 0;
		for (int i = 0; i < imgCols; i++) {
			for (int j = 0; j < imgRows; j++) {
				if (imgMat.get(j, i) == 0) {
					areaOfWord[i]++;
				}
			}
//			if (areaOfWord[i] < 60)
//				System.out.println(areaOfWord[i]);
			if(isInWord == 0){
				if (areaOfWord[i] != 0 ){
						isInWord = 1;
						currentCharStart = i;
						widthOfSpace = i - startOfSpace;
						if(widthOfSpace != 0)
							spaceAndWidth.add(new Spaces(startOfSpace,widthOfSpace));
				}
				else if(i == imgCols-1){
//					System.out.println(areaOfWord[i]);
					widthOfSpace = i - startOfSpace;
					if(widthOfSpace != 0)
						spaceAndWidth.add(new Spaces(startOfSpace,widthOfSpace));
				}
			}
			else{
				if (areaOfWord[i] == 0 ){
					isInWord = 0;
					startOfSpace = i;
					currentCharWidth = i - currentCharStart;
					if(currentCharWidth != 0)
						wordLen.add(currentCharWidth);
				}
				else if (i == imgCols-1){
					//System.out.println(areaOfWord[i]);
					currentCharWidth = i - currentCharStart;
					if(currentCharWidth != 0)
						wordLen.add(currentCharWidth);
				}
			}
		}
		Collections.sort(spaceAndWidth,Collections.reverseOrder());
		Collections.sort(wordLen);
		//System.out.println(count);
		if(wordLen.size()==0){
			returnlist.add(image);
			return returnlist;
		}
		int charMedian = wordLen.get(wordLen.size()/2);
		//System.out.println(charMedian);
		Iterator<Spaces> itr = spaceAndWidth.iterator();
		ArrayList<Spaces> spaceBetWords = new ArrayList<Spaces>();
		while (itr.hasNext()){
			Spaces spc = itr.next();
			if(spc.getWidth() < charMedian * SPACE_IN_WORD_THRE )
				break;
			spaceBetWords.add(new Spaces(spc.getWidth(),spc.getStart()));
		}
		Collections.sort(spaceBetWords);
		//Iterator<Spaces> itsp = spaceBetWords.iterator();
		int wordStart = 0;
		int pos = 1;
		int smallestSpace = spaceAndWidth.get(spaceAndWidth.size()-1).width;
		for(int i=0; i< spaceBetWords.size();i++){
			Spaces oneSpace = spaceBetWords.get(i);
			if(oneSpace.getWidth() == 0){
				continue;
			}
			IplImage clImage = cvCloneImage(image);
			IplImage blobImage;
			cvCopy(clImage, ori);
			if(i == spaceBetWords.size()-1){
				cvSetImageROI(ori, cvRect(wordStart,0,oneSpace.getWidth() + smallestSpace * 2 - wordStart,imgRows));
				wordStart = oneSpace.getWidth() + oneSpace.getStart() - smallestSpace;
			}
			else{
				cvSetImageROI(ori, cvRect(wordStart,0,oneSpace.getWidth() + smallestSpace * 2 - wordStart,imgRows));
				wordStart = oneSpace.getWidth() + oneSpace.getStart() - smallestSpace;
			}
			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
					ori.nChannels());
			cvCopy(ori, blobImage);
			cvResetImageROI(ori);
			returnlist.add(blobImage);
			//File output = new File("./src/subimage_"+ pos +"of image"+count+".jpg");
			File dir = new File("./output/image/"+abc);
			
			if(!dir.exists()){
				dir.mkdirs();
			}
			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(dir.toString()+"/"+ pos + ".jpg",blobImage);
			pos ++;	
		}
		if(wordStart + wordLen.get(wordLen.size()-1) < imgCols ){
			int width = imgCols - wordStart;
			IplImage clImage = cvCloneImage(image);
			IplImage blobImage;
			cvCopy(clImage, ori);
			cvSetImageROI(ori, cvRect(wordStart,0,width,imgRows));
			wordStart = width + wordStart - smallestSpace;
			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
					ori.nChannels());
			cvCopy(ori, blobImage);
			cvResetImageROI(ori);
			returnlist.add(blobImage);
			//File output = new File("./src/subimage_"+ pos +"of image"+count+".jpg");
			File dir = new File("./output/image/"+abc);
			if(!dir.exists()){
				dir.mkdirs();
			}
			org.bytedeco.javacpp.opencv_highgui.cvSaveImage(dir.toString()+"/"+ pos + ".jpg",blobImage);
			pos ++;	
		}
		abc++;
		return returnlist;
	}
	
}
class Spaces implements Comparable<Spaces>{
	int start;
	int width;
	Spaces(int start, int width){
		this.start = start;
		this.width = width;
	}
	@Override
	public int compareTo(Spaces o) {
		int comparedSize = o.getWidth();
		if (this.width > comparedSize) {
			return 1;
		} else if (this.width == comparedSize) {
			return 0;
		} else {
			return -1;
		}
	}
	public int getStart(){
		return start;
	}
	public int getWidth(){
		return width;
	}
}
