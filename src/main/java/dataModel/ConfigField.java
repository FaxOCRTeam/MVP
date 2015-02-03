package dataModel;

public class ConfigField {
	int[] startingPoint;
	int width;
	int height;
	String field;
	String table;

	public ConfigField(int[] startingPoint, int width, int height, String field, String table) {
		super();
		this.startingPoint = startingPoint;
		this.width = width;
		this.height = height;
		this.field = field;
		this.table = table;
	}

	public int[] getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(int[] startingPoint) {
		this.startingPoint = startingPoint;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

}
