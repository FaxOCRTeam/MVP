package gui.interfaces;

import java.io.File;

public interface FormPanelInterface {
	void loadImage(File f);

	void cancelSelection();

	void addCoordinatesNotifier(DisplayCoordinatesInterface dci);
	
	void setSelection(int[] selection);

	void resizeImage(double scale);
}
