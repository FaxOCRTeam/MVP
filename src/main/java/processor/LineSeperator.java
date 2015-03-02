package processor;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;

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
	//	ls.lineSperate(image, count);
	}
	public static List<IplImage> lineSperate(IplImage image,int count){
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
		int startOfSpace = 0, widthOfSpace = 0;
		int currentCharStart = 0, currentCharWidth = 0;
		for (int i = 0; i < imgRows; i++) {
			for (int j = 0; j < imgCols; j++) {
				if (imgMat.get(j, i) == 0) {
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
					System.out.println(areaOfLine[i]);
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
			cvSetImageROI(ori, cvRect(wordStart,0,oneSpace.getWidth() - wordStart,imgRows));
			wordStart = oneSpace.getWidth() + oneSpace.getStart();
			blobImage = cvCreateImage(cvGetSize(ori), ori.depth(),
					ori.nChannels());
			cvCopy(ori, blobImage);
			cvResetImageROI(ori);

			//File output = new File("./src/subimage_"+ pos +"of image"+count+".jpg");
			File dir = new File("./output/image" + count );
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
