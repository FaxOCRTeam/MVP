package gui.interfaces;

import gui.models.FormField;

public interface DisplayCoordinatesInterface {
	void setCoordinates(final int[] coordinates);

	void setCoordinatesAndName(FormField ff, boolean notify);

}
