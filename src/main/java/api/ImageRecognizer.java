package api;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.TesseractException;

public interface ImageRecognizer {
	String getString(BufferedImage segment) throws TesseractException;
}
