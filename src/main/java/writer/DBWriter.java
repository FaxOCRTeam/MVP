package writer;

public interface DBWriter {
	<T> void writeToDB(T t);
}
