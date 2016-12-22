package dbms;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MiniTable {
	/**
	 * TableName
	 */
	private String name;
	/**
	 * the name of the dataBase where the table will created.
	 */
	private transient Path myDB;
	/**
	 * 
	 */
	private List<Column> tableColumns = new ArrayList<Column>();

	// these fields are not saved in the generated file.
	private transient File myDataFile, myDtDFile;

	public MiniTable(String tName, Path dbPath, List<Column> cols) {
		this.name = tName;
		this.myDB = dbPath;
		this.tableColumns = cols;
		myDataFile = null;
		myDtDFile = null;
	}

	public MiniTable(String tName, Path dbPath) {
		this.name = tName;
		this.myDB = dbPath;
		this.tableColumns = null;
		myDataFile = null;
		myDtDFile = null;
	}

	public void setDataFile(File dataFile) {
		this.myDataFile = dataFile;
	}

	public void setDtDFile(File dtdFile) {
		this.myDtDFile = dtdFile;
	}

	public File getDataFile() {
		return this.myDataFile;
	}

	public File getDtDFile() {
		return this.myDtDFile;
	}

	public String getName() {
		return name;
	}

	public Path getDBPath() {
		return myDB;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the table Columns
	 */
	public List<Column> gettColumns() {
		return tableColumns;
	}

	/**
	 * @param tColumns
	 *            the table Columns to set
	 */
	public void settColumns(List<Column> tColumns) {
		this.tableColumns = tColumns;
	}
}
