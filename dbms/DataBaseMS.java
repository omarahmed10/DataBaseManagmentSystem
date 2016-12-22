package dbms;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fileManager.WriterType;

public class DataBaseMS {
	private final Logger logger;

	private Hashtable<String, DataBase> AllDataBases;
	private DataBase currentDB;

	public DataBaseMS() {
		logger = LogManager.getLogger();

		AllDataBases = new Hashtable<String, DataBase>();
	}

	public void dropTable(String tableName) throws SQLException {
		logger.debug("Dropping table : " + tableName);

		this.currentDB.dropTable(tableName);
	}

	public void insertIntoTable(String tableName, List<String> columnsName, List<String> values) throws SQLException {
		logger.debug("Inserting into table : " + tableName + "  values : " + values + "  in columns : " + columnsName);

		this.currentDB.insertIntoTable(tableName, columnsName, values);
	}

	public void insertIntoTable(String tableName, List<String> values) throws SQLException {
		logger.debug("Inserting into table : " + tableName + "  values : " + values);

		this.currentDB.insertIntoTable(tableName, values);
	}

	public void createTable(String tableName, Path dbName, List<Column> cols, WriterType fileType) throws SQLException {
		logger.debug("Creating table : " + tableName + "  with columns : " + cols + "  in format :" + fileType);

		this.currentDB.createTable(tableName, dbName, cols, fileType);
	}

	public void alterAdd(String tableName, List<Column> cols) throws SQLException {
		logger.debug("Altering 'add' columns : " + cols + "  in table : " + tableName);

		this.currentDB.alterAdd(tableName, cols);
	}

	public void alterDelete(String tableName, List<String> cols) throws SQLException {
		logger.debug("Altering 'delete' columns : " + cols + "  in table : " + tableName);

		this.currentDB.alterDelete(tableName, cols);
	}

	public List<Column> selectFromTable(String tableName, List<String> columnsName, List<Integer> rowNumber)
			throws SQLException {
		logger.debug("Selecting columns : " + columnsName + " with rows of numbers : " + rowNumber + "  from table : "
				+ tableName);

		return this.currentDB.selectFromTable(tableName, columnsName, rowNumber);
	}

	public List<Column> selectFromTable(String tableName, List<String> columnsName) throws SQLException {
		logger.debug("Selecting columns : " + columnsName + "  from table : " + tableName);

		return this.currentDB.selectFromTable(tableName, columnsName);
	}

	public List<Column> selectDistinctFromTable(String tableName, List<String> columnsName) throws SQLException {
		logger.debug("Selecting distinct columns : " + columnsName + "  from table : " + tableName);

		return this.currentDB.selectDistinct(tableName, columnsName);
	}

	public List<Column> selectDistinctFromTable(String tableName, List<String> columnsName, List<Integer> rowNumber)
			throws SQLException {
		logger.debug("Selecting distinct columns : " + columnsName + " with rows of numbers : " + rowNumber
				+ "  from table : " + tableName);

		return this.currentDB.selectDistinct(tableName, columnsName, rowNumber);
	}

	public void deleteFromTable(String tableName, List<Integer> rowNumber) throws SQLException {
		logger.debug("Deleting rows of numbers : " + rowNumber + "  from table : " + tableName);

		this.currentDB.deleteFromTable(tableName, rowNumber);
	}

	public void updateTable(String tableName, List<String> columnsName, List<String> values) throws SQLException {
		logger.debug("Updating columns : " + columnsName + " with values : " + values + "  in table : " + tableName);

		this.currentDB.updateTable(tableName, columnsName, values);
	}

	public void updateTable(String tableName, List<String> columnsName, List<String> values, List<Integer> rowNumber)
			throws SQLException {
		logger.debug("Updating columns : " + columnsName + " with rows of numbers : " + rowNumber + " with values : "
				+ values + "  from table : " + tableName);

		this.currentDB.updateTable(tableName, columnsName, values, rowNumber);
	}

	public void createDB(Path dbPath, String dbName) throws SQLException {
		logger.debug("Creating database : " + dbName);
		
		if (AllDataBases != null && AllDataBases.get(dbName.toLowerCase()) != null) {
			logger.fatal("Database \"" + dbName + "\" already exists");
			
			throw new SQLException("existing database");
		}
		DataBase newDB = new DataBase();
		newDB.createDB(dbPath, dbName);
		AllDataBases.put(dbName.toLowerCase(), newDB);
		this.currentDB = newDB;
	}

	public void dropDB(String dataBaseName) throws SQLException {
		logger.debug("Droping database : " + dataBaseName);
		
		DataBase tempDataBase = AllDataBases.get(dataBaseName.toLowerCase());
		if (tempDataBase != null) {
			this.currentDB = tempDataBase;
			this.currentDB.dropDB();
			AllDataBases.remove(dataBaseName.toLowerCase());
			this.currentDB = null;
		} else {
			logger.fatal("Database \"" + dataBaseName + "\" is not found");
			
			throw new SQLException("database not found");
		}
	}

	public void useDB(String dataBaseName) throws SQLException {
		logger.debug("Using database : " + dataBaseName);
		
		DataBase tempDataBase = AllDataBases.get(dataBaseName.toLowerCase());
		if (tempDataBase != null) {
			this.currentDB = tempDataBase;
		} else {
			logger.fatal("Database \"" + dataBaseName + "\" is not found");
			
			throw new SQLException("dataBase not found");
		}
	}

	public void useTable(String tableName) throws SQLException {
		if (this.currentDB == null) {
			logger.fatal("Table \"" + tableName + "\" is not found");
			
			throw new SQLException();
		}
		this.currentDB.useTable(tableName);
	}

}
