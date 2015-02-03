package gui.models;

public class FormField {
	String name;
	int[] rectConfig;

	public FormField(String name, int[] rectConfig) {
		super();
		this.name = name;
		this.rectConfig = rectConfig;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getRectConfig() {
		return rectConfig;
	}

	public void setRectConfig(int[] rectConfig) {
		this.rectConfig = rectConfig;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("name").append(":").append("\"").append(name).append("\"");
		sb.append(",");
		sb.append("rectConfig").append(":");
		sb.append("[");
		for (int c : rectConfig) {
			sb.append(c).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");
		return sb.toString();
	}
}