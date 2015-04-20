package api;

import java.util.List;

import dataModel.Field;

public interface DBWriter {
	void writeToDB(List<Field> fields);
}
