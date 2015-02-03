package gui;

import java.io.File;

public interface FormPanelInterface {
	void loadImage(File f);
	void cancelSelection();
	void addCoordinatesNotifier(DisplayCoordinatesInterface dci);
}
