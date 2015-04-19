package services.dao.Fax2EMR.dao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import services.dao.Fax2EMR.model.Fax2EMRLaboratoryReport;
import daogenerator.services.interfaces.Callback;
import daogenerator.utils.SqlConstructUtil;
import daogenerator.utils.StringsBuildUtil;
import daogenerator.utils.connectionPool.ConnectionPool;
import daogenerator.utils.connectionPool.DBConnection;

public class Fax2EMRLaboratoryReportDAO {
	static ConnectionPool pool = ConnectionPool.getInstance();
	static Fax2EMRLaboratoryReportDAO instance = null;

	public static Fax2EMRLaboratoryReportDAO getInstance() {
		if (null == instance) {
			instance = new Fax2EMRLaboratoryReportDAO("fax2emr");
		}
		return instance;
	}
	
	public static Fax2EMRLaboratoryReportDAO getNewInstance(String dbName) {
		return new Fax2EMRLaboratoryReportDAO(dbName);
	}
	
	private final String dbName;
	private Fax2EMRLaboratoryReportDAO(String dbName) {
		this.dbName = dbName;
	}
	
	public void truncate() {
		String sql = String.format("TRUNCATE %s.laboratory_report", dbName);
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	public int insertReturnId(Fax2EMRLaboratoryReport obj) {
		String sql = String.format("INSERT INTO %s.laboratory_report(id,Patient_name,Phone_number,Patient_ID,Physician,Sex,Age,Date_of_birth,Sodium,Potassium,Chloride,Carbon_dioxide,Calcium,Alkaline_phosphatase,AST,ALT,Bilirubin_total,Glucose) VALUES ", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			_constructPS(ps, obj, 0);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs.next())
				return rs.getInt(1);
			else
				return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
		return -1;
	}

	public void insert(Fax2EMRLaboratoryReport obj) {
		String sql = String.format("INSERT INTO %s.laboratory_report(id,Patient_name,Phone_number,Patient_ID,Physician,Sex,Age,Date_of_birth,Sodium,Potassium,Chloride,Carbon_dioxide,Calcium,Alkaline_phosphatase,AST,ALT,Bilirubin_total,Glucose) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			_constructPS(ps, obj, 0);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	/**
	 * @deprecated using insertMultiple instead
	 */
	@Deprecated
	public void insertBatch(List<Fax2EMRLaboratoryReport> objList) {
		String sql = String.format("INSERT INTO %s.laboratory_report(id,Patient_name,Phone_number,Patient_ID,Physician,Sex,Age,Date_of_birth,Sodium,Potassium,Chloride,Carbon_dioxide,Calcium,Alkaline_phosphatase,AST,ALT,Bilirubin_total,Glucose) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (Fax2EMRLaboratoryReport obj : objList) {
				_constructPS(ps, obj, 0);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	public void insertMultiple(List<Fax2EMRLaboratoryReport> objList) {
		if (objList.size() == 0)
			return;
		StringBuilder sqlBuilder = new StringBuilder(String.format("INSERT INTO %s.laboratory_report(id,Patient_name,Phone_number,Patient_ID,Physician,Sex,Age,Date_of_birth,Sodium,Potassium,Chloride,Carbon_dioxide,Calcium,Alkaline_phosphatase,AST,ALT,Bilirubin_total,Glucose) VALUES ", dbName));
		for (int i = 0; i < objList.size(); i++) {
			if (i != 0)
				sqlBuilder.append(",");
			sqlBuilder.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		}
		String sql = sqlBuilder.toString();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			int indexCount = 0;
			ps = conn.prepareStatement(sql);
			for (Fax2EMRLaboratoryReport obj : objList) {
				indexCount = _constructPS(ps, obj, indexCount);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	public void insertMultipleBatch(List<Fax2EMRLaboratoryReport> objList, int multipleSize) {
		if (objList.size() == 0)
			return;
		String sqlHead = String.format("INSERT INTO %s.laboratory_report(id,Patient_name,Phone_number,Patient_ID,Physician,Sex,Age,Date_of_birth,Sodium,Potassium,Chloride,Carbon_dioxide,Calcium,Alkaline_phosphatase,AST,ALT,Bilirubin_total,Glucose) VALUES ", dbName);
		StringBuilder sqlBuilder = new StringBuilder(sqlHead);
		for (int i = 0; i < multipleSize; i++) {
			if (i != 0)
				sqlBuilder.append(",");
			sqlBuilder.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		}
		String sql = sqlBuilder.toString();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		int startIndex = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			while (startIndex + multipleSize <= objList.size()) {
				int indexCount = 0;
				for (Fax2EMRLaboratoryReport obj : objList.subList(startIndex, startIndex + multipleSize)) {
					indexCount = _constructPS(ps, obj, indexCount);
				}
				ps.addBatch();
				startIndex += multipleSize;
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
		if (startIndex < objList.size()) {
			insertMultiple(objList.subList(startIndex, objList.size()));
		}
	}

	/**
	 * @deprecated using insertMultipleWithLimitedWindow instead
	 */
	@Deprecated
	public void insertBatchWithLimitedWindow(List<Fax2EMRLaboratoryReport> objList, int windowSize) {
		int startIndex = 0;
		int displayCounter = 0;
		while (startIndex < objList.size()) {
			insertBatch(objList.subList(startIndex, Math.min(startIndex + windowSize, objList.size())));
			startIndex += windowSize;
			if (++displayCounter % 20 == 0)
				System.out.println(String.format("[inserting Fax2EMRLaboratoryReport] %d / %d", startIndex, objList.size()));
		}
	}

	@Deprecated
	public void insertMultipleWithLimitedWindow(List<Fax2EMRLaboratoryReport> objList, int windowSize) {
		int startIndex = 0;
		int displayCounter = 0;
		while (startIndex < objList.size()) {
			insertMultiple(objList.subList(startIndex, Math.min(startIndex + windowSize, objList.size())));
			startIndex += windowSize;
			if (++displayCounter % 20 == 0)
				System.out.println(String.format("[inserting Fax2EMRLaboratoryReport] %d / %d", startIndex, objList.size()));
		}
	}

	public void insertMultipleBatchWithLimitedWindow(List<Fax2EMRLaboratoryReport> objList, int batchWindowSize, int batchMount) {
		int startIndex = 0;
		long t0 = System.currentTimeMillis();
		while (startIndex < objList.size()) {
			insertMultipleBatch(objList.subList(startIndex, Math.min(startIndex + batchMount * batchWindowSize, objList.size())), batchWindowSize);
			startIndex += batchMount * batchWindowSize;
			System.out.println(String.format("[inserting Fax2EMRLaboratoryReport] %d / %d, cost %d ms, total estimation %d ms", startIndex, objList.size(), //
					(System.currentTimeMillis() - t0), (System.currentTimeMillis() - t0) * objList.size() / startIndex));
		}
	}
	
	/**
	 * update all field anchor by field <b>id</b>
	 */
	public void update(Fax2EMRLaboratoryReport obj) {
		String sql = String.format("UPDATE %s.laboratory_report SET id = ?,Patient_name = ?,Phone_number = ?,Patient_ID = ?,Physician = ?,Sex = ?,Age = ?,Date_of_birth = ?,Sodium = ?,Potassium = ?,Chloride = ?,Carbon_dioxide = ?,Calcium = ?,Alkaline_phosphatase = ?,AST = ?,ALT = ?,Bilirubin_total = ?,Glucose = ? WHERE id = ?", dbName);
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			int indexCount = _constructPS(ps, obj, 0);
			ps.setInt(++indexCount, obj.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	/**
	 * batch update all field anchor by each elements' field <b>id</b>
	 */
	public void updateBatch(List<Fax2EMRLaboratoryReport> objList) {
		String sql = String.format("UPDATE %s.laboratory_report SET id = ?,Patient_name = ?,Phone_number = ?,Patient_ID = ?,Physician = ?,Sex = ?,Age = ?,Date_of_birth = ?,Sodium = ?,Potassium = ?,Chloride = ?,Carbon_dioxide = ?,Calcium = ?,Alkaline_phosphatase = ?,AST = ?,ALT = ?,Bilirubin_total = ?,Glucose = ? WHERE id = ?", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			int count = 0;
			for (Fax2EMRLaboratoryReport obj : objList) {
				if(++count % 5000 == 0) 
					System.out.println(String.format("[batch updating Fax2EMRLaboratoryReport] %d / %d", count, objList.size()));
				int indexCount = _constructPS(ps, obj, 0);
				ps.setInt(++indexCount, obj.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}
	
	/**
	 * 	Batch(<b><i>i</i></b> ):
	 *		UPDATE laboratory_report SET <b>setField</b> = <b>setToValueList[<i>i</i> ]</b> WHERE <b>whereField</b> IN 
	 *		(<b>whereInValueList[<i>i</i> ][0 : length]</b>)  
	 */
	public void updateBatchWithIntegerFieldSelection(String whereField, List<List<Integer>> whereInValueList, //
			String setField, List<Integer> setToValueList) {
		if (null == whereInValueList || whereInValueList.size() == 0) {
			return;
		} else if (whereInValueList.size() != setToValueList.size()) {
			new Exception("Mismatch parameter size").printStackTrace();
			return;
		}

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);

			for (int i = 0; i < whereInValueList.size(); i++) {
				int whereParamCount = whereInValueList.size();
				StringBuilder sqlBuilder = new StringBuilder( //
						String.format("UPDATE %s.laboratory_report SET %s = ? WHERE %s IN (", dbName, setField, whereField));
				for (int j = 0; j < whereParamCount; j++) {
					sqlBuilder.append('?').append(',');
				}
				sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

				String sql = sqlBuilder.toString();
				ps = conn.prepareStatement(sql);

				SqlConstructUtil.__safeSetInt(ps, 1, setToValueList.get(i));
				for (int j = 0; j < whereParamCount; j++) {
					SqlConstructUtil.__safeSetInt(ps, j + 2, whereInValueList.get(i).get(j));
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}
	
	/**
	 * UPDATE laboratory_report SET <b>setField</b> = <b>setToValue</b> WHERE <b>whereField</b> IN (<b>whereInValue[0 : length]</b>)
	 */
	public void updateWithIntegerFieldSelection(String whereField, List<Integer> whereInValue, String setField, Integer setToValue) {
		if (null == whereInValue || whereInValue.size() == 0 ) {
			return ;
		}

		StringBuilder sqlBuilder = new StringBuilder( //
			String.format("UPDATE %s.laboratory_report SET %s = ? WHERE %s IN (", dbName, setField, whereField));
		for (int i = 0; i < whereInValue.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = sqlBuilder.toString();
		
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetInt(ps, 1, setToValue);
			for (int i = 0; i < whereInValue.size(); i++) {
				SqlConstructUtil.__safeSetInt(ps, i + 2, whereInValue.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		} 
	}

	/**
	 * 	Batch(<b><i>i</i></b> ):
	 *		UPDATE laboratory_report SET <b>setField</b> = <b>setToValueList[<i>i</i> ]</b> WHERE <b>whereField</b> IN 
	 *		(<b>whereInValueList[<i>i</i> ][0 : length]</b>)  
	 *
	 */
	public void updateBatchWithStringFieldSelection(String whereField, List<List<String>> whereInValueList, //
			String setField, List<String> setToValueList) {
		if (null == whereInValueList || whereInValueList.size() == 0) {
			return;
		} else if (whereInValueList.size() != setToValueList.size()) {
			new Exception("Mismatch parameter size").printStackTrace();
			return;
		}

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);

			for (int i = 0; i < whereInValueList.size(); i++) {
				int whereParamCount = whereInValueList.size();
				StringBuilder sqlBuilder = new StringBuilder( //
						String.format("UPDATE %s.laboratory_report SET %s = ? WHERE %s IN (", dbName, setField, whereField));
				for (int j = 0; j < whereParamCount; j++) {
					sqlBuilder.append('?').append(',');
				}
				sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

				String sql = sqlBuilder.toString();
				ps = conn.prepareStatement(sql);

				SqlConstructUtil.__safeSetString(ps, 1, setToValueList.get(i));
				for (int j = 0; j < whereParamCount; j++) {
					SqlConstructUtil.__safeSetString(ps, j + 2, whereInValueList.get(i).get(j));
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}
	
	/**
	 * UPDATE laboratory_report SET <b>setField</b> = <b>setToValue</b> WHERE <b>whereField</b> IN (<b>whereInValue[0 : length]</b>)
	 */
	public void updateWithStringFieldSelection(String whereField, List<String> whereInValue, String setField, String setToValue) {
		if (null == whereInValue || whereInValue.size() == 0 ) {
			return ;
		}

		StringBuilder sqlBuilder = new StringBuilder( //
			String.format("UPDATE %s.laboratory_report SET %s = ? WHERE %s IN (", dbName, setField, whereField));
		for (int i = 0; i < whereInValue.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = sqlBuilder.toString();
		
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetString(ps, 1, setToValue);
			for (int i = 0; i < whereInValue.size(); i++) {
				SqlConstructUtil.__safeSetString(ps, i + 2, whereInValue.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		} 
	}

	public void delete(Integer id) {
		String sql = String.format("DELETE FROM %s.laboratory_report WHERE id = ?", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	@Deprecated
	public void deleteBatch(List<Integer> idList) {
		String sql = String.format("DELETE FROM %s.laboratory_report WHERE id = ?", dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			int count = 0;
			for (Integer id : idList) {
				if(++count % 5000 == 0) 
					System.out.println(String.format("[batch deleting Fax2EMRLaboratoryReport] %d / %d", count, idList.size()));
				ps.setInt(1, id);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
	}

	public void deleteMultipleById(List<Integer> idList) {
		if (null == idList || idList.size() == 0 ) 
			return ;
			
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM %s.laboratory_report WHERE id IN (");
		for (int i = 0; i < idList.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = String.format(sqlBuilder.toString(), dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < idList.size(); i++) {
				ps.setInt(i + 1, idList.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		} 
	}
	
	public void deleteMultipleBatchById(List<Integer> idList, int multipleSize) {
		if (idList.size() == 0)
			return;
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM %s.laboratory_report WHERE id IN (");
		for (int i = 0; i < multipleSize; i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
		
		String sql = String.format(sqlBuilder.toString(), dbName);
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		int startIndex = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			while (startIndex + multipleSize <= idList.size()) {
				List<Integer> subList = idList.subList(startIndex, startIndex + multipleSize);
				for (int i = 0; i < subList.size(); i++) {
					ps.setInt(i + 1, subList.get(i));
				}
				ps.addBatch();
				startIndex += multipleSize;
			}
			ps.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps);
		}
		if (startIndex < idList.size()) {
			deleteMultipleById(idList.subList(startIndex, idList.size()));
		}
	}

	public void deleteMultipleBatchByIdWithLimitedWindow(List<Integer> idList, int batchWindowSize, int batchMount) {
		int startIndex = 0;
		long t0 = System.currentTimeMillis();
		while (startIndex < idList.size()) {
			deleteMultipleBatchById(idList.subList(startIndex, Math.min(startIndex + batchMount * batchWindowSize, idList.size())), batchWindowSize);
			startIndex += batchMount * batchWindowSize;
			System.out.println(String.format("[deleting Fax2EMRLaboratoryReport] %d / %d, cost %d ms, total estimation %d ms", startIndex, idList.size(), //
					(System.currentTimeMillis() - t0), (System.currentTimeMillis() - t0) * idList.size() / startIndex));
		}
	}

	public Integer selectMaxId() {
		Integer result = null;
		String sql = String.format("SELECT MAX(id) FROM %s.laboratory_report", dbName);
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt("MAX(id)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public Fax2EMRLaboratoryReport selectById(Integer id) {
		if (null == id) {
			return null;
		}
		String sql = String.format("SELECT * FROM %s.laboratory_report WHERE id = ?", dbName);
		Fax2EMRLaboratoryReport result = null;
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = _constructResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByIdList(List<Integer> idList) {
		if (null == idList) {
			return null;
		} else if (idList.size() == 0) {
			return new ArrayList<Fax2EMRLaboratoryReport>();
		}

		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM %s.laboratory_report WHERE id IN (");
		for (int i = 0; i < idList.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = String.format(sqlBuilder.toString(), dbName);

		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < idList.size(); i++) {
				ps.setInt(i + 1, idList.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(_constructResult(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByIdListWithLimitedWindow(List<Integer> idList, Integer windowSize) {
		if (null == idList)
			return null;
		
		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		int paramterListStartIndex = 0;
		while (paramterListStartIndex < idList.size()) {
			System.out.println(String.format("[Selecting Id]\t%d / %d", paramterListStartIndex, idList.size()));
			List<Integer> partialIdList = new ArrayList<Integer>();
			int subListEnd = Math.min(paramterListStartIndex + windowSize, idList.size());
			partialIdList.addAll(idList.subList(paramterListStartIndex, subListEnd));
			result.addAll(selectByIdList(partialIdList));
			
			partialIdList.clear();
			paramterListStartIndex += windowSize;
		}
		
		return result;
	}

	public Fax2EMRLaboratoryReport selectSingleByStringField(String field, String value) {
		if (field == null || "".equals(field.trim())) {
			return null;
		}
		String sql = String.format("SELECT * FROM %s.laboratory_report WHERE %s = ? LIMIT 1", dbName, field);
		Fax2EMRLaboratoryReport result = null;
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetString(ps, 1, value);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = _constructResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByStringField(String field, String value) {
		if (field == null || "".equals(field.trim())) {
			return null;
		}
		String sql = String.format("SELECT * FROM %s.laboratory_report WHERE %s = ?", dbName, field);
		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetString(ps, 1, value);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(_constructResult(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByMultipleStringField(String field, List<String> valueList) {
		if (null == valueList) {
			return null;
		} else if (valueList.size() == 0) {
			return new ArrayList<Fax2EMRLaboratoryReport>();
		}

		StringBuilder sqlBuilder = new StringBuilder(String.format("SELECT * FROM %s.laboratory_report WHERE %s IN (", dbName, field));
		for (int i = 0; i < valueList.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = sqlBuilder.toString();

		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < valueList.size(); i++) {
				SqlConstructUtil.__safeSetString(ps, i + 1, valueList.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(_constructResult(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByMultipleStringFieldWithLimitedWindow(String field, List<String> valueList, Integer windowSize) {
		if (null == valueList)
			return null;

		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		int paramterListStartIndex = 0;
		while (paramterListStartIndex < valueList.size()) {
			System.out.println(String.format("[Selecting String Field \"%s\"]\t%d / %d", field, paramterListStartIndex, valueList.size()));
			List<String> partialIdList = new ArrayList<String>();
			int subListEnd = Math.min(paramterListStartIndex + windowSize, valueList.size());
			partialIdList.addAll(valueList.subList(paramterListStartIndex, subListEnd));
			result.addAll(selectByMultipleStringField(field, partialIdList));
			
			partialIdList.clear();
			paramterListStartIndex += windowSize;
		}

		return result;
	}

	public Fax2EMRLaboratoryReport selectSingleByIntegerField(String field, Integer value) {
		if (field == null || "".equals(field.trim())) {
			return null;
		}
		String sql = String.format("SELECT * FROM %s.laboratory_report WHERE %s = ? LIMIT 1", dbName, field);
		Fax2EMRLaboratoryReport result = null;
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetInt(ps, 1, value);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = _constructResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByIntegerField(String field, Integer value) {
		if (field == null || "".equals(field.trim())) {
			return null;
		}
		String sql = String.format("SELECT * FROM %s.laboratory_report WHERE %s = ?", dbName, field);
		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			SqlConstructUtil.__safeSetInt(ps, 1, value);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(_constructResult(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByMultipleIntegerField(String field, List<Integer> valueList) {
		if (null == valueList) {
			return null;
		} else if (valueList.size() == 0) {
			return new ArrayList<Fax2EMRLaboratoryReport>();
		}

		StringBuilder sqlBuilder = new StringBuilder(String.format("SELECT * FROM %s.laboratory_report WHERE %s IN (", dbName, field));
		for (int i = 0; i < valueList.size(); i++) {
			sqlBuilder.append('?').append(',');
		}
		sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');

		String sql = sqlBuilder.toString();

		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < valueList.size(); i++) {
				SqlConstructUtil.__safeSetInt(ps, i+1, valueList.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(_constructResult(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> selectByMultipleIntegerFieldWithLimitedWindow(String field, List<Integer> valueList, Integer windowSize) {
		if (null == valueList)
			return null;

		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		int paramterListStartIndex = 0;
		while (paramterListStartIndex < valueList.size()) {
			System.out.println(String.format("[Selecting Integer Field \"%s\"]\t%d / %d", field, paramterListStartIndex, valueList.size()));
			List<Integer> partialIdList = new ArrayList<Integer>();
			int subListEnd = Math.min(paramterListStartIndex + windowSize, valueList.size());
			partialIdList.addAll(valueList.subList(paramterListStartIndex, subListEnd));
			result.addAll(selectByMultipleIntegerField(field, partialIdList));

			partialIdList.clear();
			paramterListStartIndex += windowSize;
		}

		return result;
	}


	public List<Fax2EMRLaboratoryReport> walk(List<Field> fieldList, int start, int limit) {
		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();

		boolean defaultField = null == fieldList || fieldList.size() == 0;
		String fieldString = "*";
		if (!defaultField) {
			StringBuilder fieldStringBuilder = new StringBuilder();
			fieldStringBuilder.append("id, ");
			for (Field field : fieldList)
				fieldStringBuilder.append(StringsBuildUtil.escapeSystemKeyword(field.getName(), true)).append(", ");
			fieldStringBuilder.delete(fieldStringBuilder.length() - 2, fieldStringBuilder.length() - 1);
			fieldString = fieldStringBuilder.toString();
		}
		String sql = String.format("SELECT %s FROM %s.laboratory_report WHERE id >= ? ORDER BY id LIMIT ?", fieldString, dbName);

		DBConnection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, limit);
			rs = ps.executeQuery();
			while (rs.next()) {
				if(defaultField)
					result.add(_constructResult(rs));
				else {
					Fax2EMRLaboratoryReport obj = SqlConstructUtil._constructResult(fieldList, Fax2EMRLaboratoryReport.class, rs);
					obj.setId(rs.getInt("id"));
					result.add(obj);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.close(conn, ps, rs);
		}
		return result;
	}

	public List<Fax2EMRLaboratoryReport> walk(int start, int limit) {
		return walk(null, start, limit);
	}

	public List<Fax2EMRLaboratoryReport> walkAll() {
		return walkAll(null, -1);
	}

	public List<Fax2EMRLaboratoryReport> walkAll(int walkstep) {
		return walkAll(null, walkstep);
	}
	
	/**
	 * walk given size of data from DB.
	 * 
	 * @param walkstep
	 *            the number of step, -1 stands for all data, each step contains 500 lines.
	 */

	public List<Fax2EMRLaboratoryReport> walkAll(List<Field> fieldList, int walkstep) {
		long t0 = System.currentTimeMillis();
		int stepCount = 0;
		List<Fax2EMRLaboratoryReport> result = new ArrayList<Fax2EMRLaboratoryReport>();
		int startid = 0;
		int limit = 500;
		while (true) {
			List<Fax2EMRLaboratoryReport> walk = walk(startid, limit);
			if (null == walk || walk.size() == 0)
				break;
			result.addAll(walk);
			startid = walk.get(walk.size() - 1).getId() + 1;
			stepCount++;
			if (stepCount % 20 == 0) {
				System.out.println(String.format("[Loading Fax2EMRLaboratoryReport] id:%d, timeUsed:%dms", startid, (System.currentTimeMillis() - t0)));
			}
			if (-1 != walkstep && stepCount >= walkstep) {
				break;
			}
		}
		return result;
	}

	public void fetchAll(Callback<Fax2EMRLaboratoryReport> callback) {
		fetchAll(callback, -1);
	}

	public void fetchAll(Callback<Fax2EMRLaboratoryReport> callback, int fetchstep) {
		fetchAll(callback, null, fetchstep);
	}
	
	public void fetchAll(Callback<Fax2EMRLaboratoryReport> callback, List<Field> fieldList) {
		fetchAll(callback, fieldList, -1);
	}
	
	public void fetchAll(Callback<Fax2EMRLaboratoryReport> callback, List<Field> fieldList, int fetchstep) {
		long t0 = System.currentTimeMillis();
		int stepCount = 0;
		int startid = 0;
		int limit = 500;
		while (true) {
			List<Fax2EMRLaboratoryReport> walk = walk(fieldList, startid, limit);
			if (null == walk || walk.size() == 0)
				break;

			callback.process(walk);

			startid = walk.get(walk.size() - 1).getId() + 1;
			stepCount++;
			if (stepCount % 20 == 0) {
				System.out.println(String.format("[Fetching Fax2EMRLaboratoryReport] id:%d, timeUsed:%dms", startid, (System.currentTimeMillis() - t0)));
			}
			if (-1 != fetchstep && stepCount >= fetchstep) {
				break;
			}
		}
	}

	public List<Field> _getFieldList(String[] fieldNameArray) throws NoSuchFieldException {
		List<Field> result = new ArrayList<Field>();
		try {
			for (String fieldName : fieldNameArray) {
				result.add(Fax2EMRLaboratoryReport.class.getDeclaredField(fieldName));
			}
		} catch (NoSuchFieldException e) {
			throw e;
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int _constructPS(PreparedStatement ps, Fax2EMRLaboratoryReport obj, int indexCount) throws SQLException {
		SqlConstructUtil.__safeSetInt(ps, ++indexCount, obj.getId());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getPatientName());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getPhoneNumber());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getPatientID());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getPhysician());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getSex());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getAge());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getDateOfBirth());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getSodium());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getPotassium());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getChloride());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getCarbonDioxide());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getCalcium());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getAlkalinePhosphatase());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getAST());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getALT());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getBilirubinTotal());
		SqlConstructUtil.__safeSetString(ps, ++indexCount, obj.getGlucose());

		return indexCount;
	}

	public Fax2EMRLaboratoryReport _constructResult(ResultSet rs) throws SQLException {
		Fax2EMRLaboratoryReport obj = new Fax2EMRLaboratoryReport();
		obj.setId(rs.getInt("id"));
		obj.setPatientName(rs.getString("Patient_name"));
		obj.setPhoneNumber(rs.getString("Phone_number"));
		obj.setPatientID(rs.getString("Patient_ID"));
		obj.setPhysician(rs.getString("Physician"));
		obj.setSex(rs.getString("Sex"));
		obj.setAge(rs.getString("Age"));
		obj.setDateOfBirth(rs.getString("Date_of_birth"));
		obj.setSodium(rs.getString("Sodium"));
		obj.setPotassium(rs.getString("Potassium"));
		obj.setChloride(rs.getString("Chloride"));
		obj.setCarbonDioxide(rs.getString("Carbon_dioxide"));
		obj.setCalcium(rs.getString("Calcium"));
		obj.setAlkalinePhosphatase(rs.getString("Alkaline_phosphatase"));
		obj.setAST(rs.getString("AST"));
		obj.setALT(rs.getString("ALT"));
		obj.setBilirubinTotal(rs.getString("Bilirubin_total"));
		obj.setGlucose(rs.getString("Glucose"));

		return obj;
	}
}

