package api;

public interface DBWriter {
	<T> void writeToDB(T t);
}
