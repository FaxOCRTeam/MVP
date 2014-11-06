package dataModel;

public class ConfigField {
	int[] startingPoint;
	int[] endingPoint;
	String field;
	String table;

	public int[] getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(int[] startingPoint) {
		this.startingPoint = startingPoint;
	}

	public int[] getEndingPoint() {
		return endingPoint;
	}

	public void setEndingPoint(int[] endingPoint) {
		this.endingPoint = endingPoint;
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
