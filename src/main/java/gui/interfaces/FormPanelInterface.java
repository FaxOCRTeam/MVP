package gui.interfaces;



import java.awt.image.BufferedImage;
import java.io.File;

import org.bytedeco.javacpp.opencv_core.IplImage;



public interface FormPanelInterface {
	void loadImage(File f);

	void cancelSelection();

	void addCoordinatesNotifier(DisplayCoordinatesInterface dci);
	
	void setSelection(int[] selection);

<<<<<<< HEAD
  void resizeImage(double scale);
=======
//	void resizeImage(double scale);
	void zoomIn();
	void zoomOut();
	void originZoom();
	
	BufferedImage getImage();
	void deskew(IplImage image) ;
>>>>>>> refs/heads/master
}

