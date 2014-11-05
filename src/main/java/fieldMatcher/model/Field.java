package fieldMatcher.model;

import java.awt.Image;

import configReader.model.ConfigField;

public class Field {
	Image Image;
	String field;
	String content;
	ConfigField config;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
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

}
