package dataModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class Field {
	BufferedImage Image;
	String field;
	String content;
	ConfigField config;
	ArrayList<ArrayList<IplImage>> imageList = new ArrayList<ArrayList<IplImage>>();

	public BufferedImage getImage() {
		return Image;
	}

	public void setImage(BufferedImage image) {
		Image = image;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ConfigField getConfig() {
		return config;
	}

	public void setConfig(ConfigField config) {
		this.config = config;
	}
	
	public ArrayList<ArrayList<IplImage>> getimageList() {
		return imageList;
	}

	public void setimageList(ArrayList<ArrayList<IplImage>> imageList) {
		this.imageList = imageList;
	}

}
