package gui.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class FormField {
	static Gson gson = new Gson();

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
		// StringBuilder sb = new StringBuilder();
		// sb.append("{");
		// sb.append("name").append(":").append("\"").append(name).append("\"");
		// sb.append(",");
		// sb.append("rectConfig").append(":");
		// sb.append("[");
		// for (int c : rectConfig) {
		// sb.append(c).append(",");
		// }
		// sb.deleteCharAt(sb.length() - 1);
		// sb.append("]}");
		// return sb.toString();
		return gson.toJson(this);
	}

	public static FormField parseObj(String json) {
		return gson.fromJson(json, FormField.class);
	}
	
	public static void saveModel(File f, List<FormField> model) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (FormField ff : model) {
			writer.println(ff.toString());
		}
		writer.close();
	}
	
	public static List<FormField> loadModel(File f) {
		List<FormField> result = new ArrayList<FormField>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				result.add(parseObj(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
