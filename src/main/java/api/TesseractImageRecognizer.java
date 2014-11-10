package api;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TesseractImageRecognizer implements ImageRecognizer {
	private Tesseract tesseract;

	public TesseractImageRecognizer() {
		tesseract = Tesseract.getInstance();
	}

	public String getString(BufferedImage segment){
		String str = null;
		try {
			str = tesseract.doOCR(segment);
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return str;
	}

}
