package dbms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fileManager.WriterType;

/**
 * Please use constructor to intialize the variables.
 *
 * @author omar
 *
 */
public class DataBase {
	private final Logger logger;
	
	private String name;
	private Path myPath;
	private TableManager tableManager;
	private Hashtable<String, MiniTable> myTables;

	public DataBase() {
		logger = LogManager.getLogger();
		
		tableManager = new TableManager();
		myTables = new Hashtable<String, MiniTable>();
	}

	public String getName() {
		return name;
	}

	public void createDB(Path dbPath, String dataBaseName) throws SQLException {
		// create DataBases folder if not found.
		this.name = dataBaseName;
		this.myPath = Paths.get(dbPath + File.separator + dataBaseName);
		if (Files.notExists(dbPath)) {
			try {
				Files.createDirectories(dbPath);
			} catch (IOException e) {
				logger.fatal("Cannot find or create main folder");
				
				throw new SQLException("cannot find or create main folder");
			}
		}
		File Dir = new File(this.myPath.toString());
		if (!Dir.mkdir()) {
			logger.fatal("Cannot create folder : " + this.myPath.getFileName());
			
			throw new SQLException("cannot create the folder");
		}
	}

	public void dropDB() throws SQLException {
		try {
			delete(this.myPath.toFile());
			// System.out.println("Bye Bye " + this.name);
		} catch (Exception e) {
			logger.fatal("Cannot delete folder " + this.myPath.getFileName());
			
			e.printStackTrace();
			throw new SQLException("cannot delete " + this.myPath.getFileName() + " folder");
		}
		this.myTables.clear();
	}

	/**
	 * to delete the directory when it have files.
	 *
	 * @param index
	 */
	private void delete(File index) {
		String[] entries = index.list();
		for (String s : entries) {
			File currentFile = new File(index.getPath(), s);
			currentFile.delete();
		}
		// check the directory again, if empty then delete it
		if (index.list().length == 0) {
			index.delete();
		}
	}

	private void checkTableExistance(String tableName) throws SQLException {
		MiniTable tempTable = this.myTables.get(tableName.toLowerCase());
		if (tempTable != null) {
			this.tableManager.setCurrentTable(tempTable);
		} else {
			logger.fatal("Table " + tableName + " is not found");
			
			throw new SQLException("table " + tableName + " not found");
		}
	}

	public void useTable(String tableName) throws SQLException {
		checkTableExistance(tableName);
	}

	public void dropTable(String tableName) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.dropTable();
		myTables.remove(tableName.toLowerCase());
	}

	public void insertIntoTable(String tableName, List<String> columnsName, List<String> values) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.insert(columnsName, values);
	}

	public void insertIntoTable(String tableName, List<String> values) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.insert(values);
	}

	public void createTable(String tableName, Path dbName, List<Column> cols, WriterType fileType) throws SQLException {
		if (myTables != null && myTables.get(tableName.toLowerCase()) != null) {
			logger.fatal("Table " + tableName + " already exist");
			
			throw new SQLException("existing Table");
		}
		MiniTable newTable = this.tableManager.createTable(tableName, dbName, cols, fileType);
		this.myTables.put(tableName.toLowerCase(), newTable);
	}

	public List<Column> selectFromTable(String tableName, List<String> columnsName, List<Integer> rowNumber)
			throws SQLException {
		checkTableExistance(tableName);
		return this.tableManager.select(columnsName, rowNumber);
	}

	public List<Column> selectFromTable(String tableName, List<String> columnsName) throws SQLException {
		checkTableExistance(tableName);
		return this.tableManager.select(columnsName);
	}

	public void deleteFromTable(String tableName, List<Integer> rowNumber) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.delete(rowNumber);

	}

	public void updateTable(String tableName, List<String> columnsName, List<String> Values) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.update(columnsName, Values);
	}

	public void updateTable(String tableName, List<String> columnsName, List<String> Values, List<Integer> rowNumber)
			throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.update(columnsName, Values, rowNumber);
	}

	public void alterAdd(String tableName, List<Column> cols) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.alterAdd(cols);
	}

	public void alterDelete(String tableName, List<String> cols) throws SQLException {
		checkTableExistance(tableName);
		this.tableManager.alterDelete(cols);
	}

	public List<Column> selectDistinct(String tableName, List<String> columnsName) throws SQLException {
		checkTableExistance(tableName);
		return this.tableManager.selectDistinct(columnsName);
	}

	public List<Column> selectDistinct(String tableName, List<String> columnsName, List<Integer> rowNumber)
			throws SQLException {
		checkTableExistance(tableName);
		return this.tableManager.selectDistinct(columnsName, rowNumber);
	}
}
