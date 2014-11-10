package independent.daoGenerator;

import java.util.ArrayList;
import java.util.List;

import daogenerator.JavaModelFileBuilder;
import daogenerator.services.dao.MysqlDao;
import daogenerator.services.dao.model.MysqlColumn;

public class DAOStructureBuilder {
	
	
	public static void main(String[] args) {
		List<DBConfig> configList = new ArrayList<DAOStructureBuilder.DBConfig>();
		configList.add(new DBConfig("fax2emr", "Fax2EMR", new String[]{"patient_information"}));
		
//		JavaModelFileBuilder builder = new JavaModelFileBuilder("D:\\Users\\yxy\\DataUpdate\\DAOGenerator\\src", "services.dao");
		JavaModelFileBuilder builder = new JavaModelFileBuilder();
		for(DBConfig currentConfig : configList) {
			for (String table : currentConfig.tables) {
				List<MysqlColumn> showColumnsFromTable = MysqlDao.showColumnsFromTable(currentConfig.dbName, table);
				builder.outputFile(currentConfig.packageName, table, showColumnsFromTable, currentConfig.dbName, currentConfig.packageName);
			}
		}
	}
	/**
	 * Define DBConfig as a datatype containing dbName, packageName and tables
	 * @author wenqili
	 *
	 */
	public static class DBConfig {
		String dbName; //the name of the source database
		String packageName; //the name of the target package
		String[] tables; //name of all the tables in the current database
		public DBConfig(String dbName, String packageName, String[] tables) {
			super();
			this.dbName = dbName;
			this.packageName = packageName;
			this.tables = tables;
		}
		public String getDbName() {
			return dbName;
		}
		public void setDbName(String dbName) {
			this.dbName = dbName;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String[] getTables() {
			return tables;
		}
		public void setTables(String[] tables) {
			this.tables = tables;
		}
		
		
	}
}
