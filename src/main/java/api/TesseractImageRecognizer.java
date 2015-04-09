package api;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.TessAPI.TessOcrEngineMode;
import net.sourceforge.tess4j.TessAPI.TessPageSegMode;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TesseractImageRecognizer implements ImageRecognizer {
	private Tesseract tesseract;

	public TesseractImageRecognizer() {
		tesseract = Tesseract.getInstance();
		tesseract.setOcrEngineMode(TessOcrEngineMode.OEM_TESSERACT_CUBE_COMBINED);
		tesseract.setPageSegMode(TessPageSegMode.PSM_SINGLE_WORD);
	}

	public String getString(BufferedImage segment){
		String str = null;
		try {
			str = tesseract.doOCR(segment).trim();
		//	System.out.println(str);
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return str;
	}

}
