package dbmsController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import condition.ConditionFinder;
import dbms.Column;
import dbms.DataBaseMS;
import extractor.AlterExtractor;
import extractor.CreateExtractor;
import extractor.DeleteExtractor;
import extractor.DropExtractor;
import extractor.Extractor;
import extractor.Extractor.commands;
import extractor.InsertExtractor;
import extractor.SelectExtractor;
import extractor.UpdateExtractor;
import extractor.UseExtractor;
import fileManager.WriterType;

public class Controller {
	/**
	 * read input from user.
	 */
	private final Logger logger;

	private static Scanner input;
	private static Extractor extractor;
	private static ConditionFinder conditionFinder;
	private DataBaseMS dbms;
	private List<Integer> affectedRowList;
	private List<Column> selectedDataList;
	private List<String> selectedColumnsNameList;
	private String currentDB, currentTable;
	private Path dataBasePath;
	private WriterType dbWriterType;
	private int affectedRowsNumber;
	private commands currentCommand;

	public Controller(Path dbPath, WriterType writerType) {
		logger = LogManager.getLogger();

		dbms = new DataBaseMS();
		affectedRowList = null;
		affectedRowsNumber = 0;
		conditionFinder = null;
		selectedDataList = null;
		selectedColumnsNameList = null;
		currentDB = null;
		dbWriterType = writerType;
		dataBasePath = dbPath;
	}

	private void createDB() throws SQLException {
		currentDB = extractor.getDatabaseName();
		dbms.createDB(dataBasePath, currentDB);
	}

	private void createTable() throws SQLException {
		affectedRowList = null;
		affectedRowsNumber = 0;
		List<Column> tableData = new ArrayList<Column>();
		List<String> columnsList = extractor.getColumnsNames();
		List<String> dataTypeList = extractor.getColumnsTypes();
		for (int i = 0; i < columnsList.size(); i++) {
			Column columnI = new Column();
			tableData.add(columnI);
			tableData.get(i).setName(columnsList.get(i));
			tableData.get(i).setType(dataTypeList.get(i));
		}
		currentTable = extractor.getTableName();
		dbms.createTable(currentTable,
				Paths.get(dataBasePath + File.separator + currentDB), tableData,
				dbWriterType);
	}

	private void insertIntoTable() throws SQLException {
		affectedRowList = null;
		affectedRowsNumber = 0;
		currentTable = extractor.getTableName();
		dbms.useTable(currentTable);
		if (extractor.getColumnsNames() != null
				&& extractor.getColumnsNames().size() > 0) {
			// System.out.println("ana hena yad");
			dbms.insertIntoTable(currentTable, extractor.getColumnsNames(),
					extractor.getValues());
		} else {
			dbms.insertIntoTable(currentTable, extractor.getValues());
		}
		affectedRowsNumber = 1;
		printFormatedTable(dbms.selectFromTable(currentTable, null));
	}

	private void printFormatedTable(List<Column> list) {
		// for (int i = 0; i < list.size(); i++) {
		// System.out.print(list.get(i).getName() + "\t");
		// }
		// ArrayList<String> subList = list.get(0).getColElements();
		// for (int j = 0; j < subList.size(); j++) {
		// System.out.println();
		// for (int i = 0; i < list.size(); i++) {
		// System.out.print(list.get(i).getColElements().get(j) + "\t");
		// }
		// }
		// System.out.println();
	}

	private void selectFromTable() throws SQLException {
		currentTable = extractor.getTableName();
		dbms.useTable(currentTable);
		selectedDataList = null;
		affectedRowList = null;
		affectedRowsNumber = 0;
		if (extractor.getConditionFirstOperand() != null) {
			conditionFinder = new ConditionFinder(
					extractor.getConditionFirstOperand(),
					extractor.getConditionOperator(),
					extractor.getConditionSecondOperand(), dbms, currentTable);
			affectedRowList = conditionFinder.getRowsNumber();
		}
		selectedColumnsNameList = extractor.getColumnsNames();
		// System.out.println(selectedColumnsNameList);
		if (affectedRowList == null) {
			selectedDataList = dbms.selectFromTable(currentTable,
					selectedColumnsNameList);
			printFormatedTable(selectedDataList);
		} else {
			selectedDataList = dbms.selectFromTable(currentTable,
					selectedColumnsNameList, affectedRowList);
			printFormatedTable(selectedDataList);
		}
	}

	private void selectDistinctFromTable() throws SQLException {
		currentTable = extractor.getTableName();
		dbms.useTable(currentTable);
		selectedDataList = null;
		affectedRowList = null;
		affectedRowsNumber = 0;
		if (extractor.getConditionFirstOperand() != null) {
			conditionFinder = new ConditionFinder(
					extractor.getConditionFirstOperand(),
					extractor.getConditionOperator(),
					extractor.getConditionSecondOperand(), dbms, currentTable);
			affectedRowList = conditionFinder.getRowsNumber();
		}
		selectedColumnsNameList = extractor.getColumnsNames();
		// System.out.println(selectedColumnsNameList);
		if (affectedRowList == null) {
			selectedDataList = dbms.selectDistinctFromTable(currentTable,
					selectedColumnsNameList);
			printFormatedTable(selectedDataList);
		} else {
			selectedDataList = dbms.selectDistinctFromTable(currentTable,
					selectedColumnsNameList, affectedRowList);
			printFormatedTable(selectedDataList);
		}
	}

	public String getTableName() {
		return currentTable;
	}

	public List<String> getSelectedColumnNames() {
		if (selectedDataList == null) {
			return null;
		}
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < selectedDataList.size(); i++) {
			data.add(selectedDataList.get(i).getName());
		}
		return data;
	}

	public List<String> getSelectedColumnDataType() {
		if (selectedDataList == null) {
			return null;
		}
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < selectedDataList.size(); i++) {
			// System.out.println(selectedDataList.get(i).getType());
			data.add(selectedDataList.get(i).getType());
		}
		return data;
	}

	public List<List<String>> getSelectedDataValues() {
		if (selectedDataList == null) {
			return null;
		}
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> subList = selectedDataList.get(0).getColElements();
		for (int j = 0; j < subList.size(); j++) {
			data.add(new ArrayList<String>());
			for (int i = 0; i < selectedDataList.size(); i++) {
				data.get(j)
						.add(selectedDataList.get(i).getColElements().get(j));
			}
		}
		return data;
	}

	private void useDataBase() throws SQLException {
		currentDB = extractor.getDatabaseName();
		dbms.useDB(currentDB);
	}

	private void updateTable() throws SQLException {
		String currentTable = extractor.getTableName();
		dbms.useTable(currentTable);
		affectedRowList = null;
		affectedRowsNumber = 0;
		if (extractor.getConditionFirstOperand() != null) {
			conditionFinder = new ConditionFinder(
					extractor.getConditionFirstOperand(),
					extractor.getConditionOperator(),
					extractor.getConditionSecondOperand(), dbms, currentTable);
			affectedRowList = conditionFinder.getRowsNumber();
		}
		List<String> cList = extractor.getColumnsNames();
		List<String> vList = extractor.getValues();
		if (affectedRowList == null) {
			affectedRowsNumber = dbms.selectFromTable(currentTable, null).get(0)
					.getColElements().size();
			dbms.updateTable(currentTable, cList, vList);
		} else {
			affectedRowsNumber = affectedRowList.size();
			dbms.updateTable(currentTable, cList, vList, affectedRowList);
		}
		printFormatedTable(dbms.selectFromTable(currentTable, null));
	}

	private void drop() throws SQLException {
		affectedRowList = null;
		affectedRowsNumber = 0;
		String dataBaseName = extractor.getDatabaseName();
		String tableName = extractor.getTableName();
		// table = dataBase.useTable(extractor.getTableName());
		if (extractor.getTableName() != null) {
			dbms.dropTable(tableName);
		} else {
			dbms.dropDB(dataBaseName);
		}
	}

	private void delete() throws SQLException {
		affectedRowList = null;
		currentTable = extractor.getTableName();
		dbms.useDB(currentDB);
		if (extractor.getConditionFirstOperand() != null) {
			conditionFinder = new ConditionFinder(
					extractor.getConditionFirstOperand(),
					extractor.getConditionOperator(),
					extractor.getConditionSecondOperand(), dbms, currentTable);
			affectedRowList = conditionFinder.getRowsNumber();
		}
		dbms.useTable(extractor.getTableName());
		if (affectedRowList == null) {
			// delete all table data
			affectedRowsNumber = dbms.selectFromTable(currentTable, null).get(0)
					.getColElements().size();
			dbms.deleteFromTable(currentTable, null);
		} else {
			affectedRowsNumber = affectedRowList.size();
			// System.out.println("controller>> " + affectedRowList);
			dbms.deleteFromTable(currentTable, affectedRowList);
		}
		printFormatedTable(dbms.selectFromTable(currentTable, null));
	}

	private void alterAddInTable() throws SQLException {
		currentTable = extractor.getTableName();
		affectedRowList = null;
		affectedRowsNumber = 0;
		List<Column> tableData = new ArrayList<Column>();
		List<String> columnsList = extractor.getColumnsNames();
		List<String> dataTypeList = extractor.getColumnsTypes();
		for (int i = 0; i < columnsList.size(); i++) {
			Column columnI = new Column();
			tableData.add(columnI);
			tableData.get(i).setName(columnsList.get(i));
			tableData.get(i).setType(dataTypeList.get(i));
		}
		dbms.alterAdd(currentTable, tableData);
		printFormatedTable(dbms.selectFromTable(currentTable, null));
	}

	private void alterDropFromTable() throws SQLException {
		affectedRowList = null;
		affectedRowsNumber = 0;
		currentTable = extractor.getTableName();
		List<String> columnsList = extractor.getColumnsNames();
		dbms.alterDelete(currentTable, columnsList);
		printFormatedTable(dbms.selectFromTable(currentTable, null));
	}

	public int getEffectedRowsNumber() {
		// System.out.println(affectedRowList);
		return affectedRowsNumber;
	}

	int count = 0;

	public void excuteQuery() throws SQLException {
		if (currentCommand == null) {
			logger.fatal("There is no command");
			throw new SQLException();
		}
		if (currentCommand == commands.CREATEDB) {
			createDB();
		} else if (currentCommand == commands.CREATETABLE) {
			createTable();
		} else if (currentCommand == commands.INSERT) {
			insertIntoTable();
		} else if (currentCommand == commands.SELECT) {
			selectFromTable();
		} else if (currentCommand == commands.SELECTDISTINCT) {
			selectDistinctFromTable();
		} else if (currentCommand == commands.UPDATE) {
			updateTable();
		} else if (currentCommand == commands.DELETE) {
			delete();
		} else if (currentCommand == commands.DROP) {
			drop();
		} else if (currentCommand == commands.USE) {
			useDataBase();
		} else if (currentCommand == commands.ALTERADD) {
			alterAddInTable();
		} else if (currentCommand == commands.ALTERDROP) {
			alterDropFromTable();
		}
	}


	public void setQuery(String txt) throws SQLException {
		CreateExtractor createExtractor = new CreateExtractor(txt);
		InsertExtractor insertExtractor = new InsertExtractor(txt);
		SelectExtractor selectExtractor = new SelectExtractor(txt);
		UpdateExtractor updateExtractor = new UpdateExtractor(txt);
		DeleteExtractor deleteExtractor = new DeleteExtractor(txt);
		DropExtractor dropExtractor = new DropExtractor(txt);
		UseExtractor useExtractor = new UseExtractor(txt);
		AlterExtractor alterExtractor = new AlterExtractor(txt);
		if (createExtractor.isValid()) {
			extractor = createExtractor;
			currentCommand = extractor.getCommand();
		} else if (insertExtractor.isValid()) {
			extractor = insertExtractor;
			currentCommand = extractor.getCommand();
		} else if (selectExtractor.isValid()) {
			extractor = selectExtractor;
			currentCommand = selectExtractor.getCommand();
		} else if (updateExtractor.isValid()) {
			extractor = updateExtractor;
			currentCommand = extractor.getCommand();
		} else if (deleteExtractor.isValid()) {
			extractor = deleteExtractor;
			currentCommand = extractor.getCommand();
		} else if (dropExtractor.isValid()) {
			extractor = dropExtractor;
			currentCommand = extractor.getCommand();
		} else if (useExtractor.isValid()) {
			extractor = useExtractor;
			currentCommand = extractor.getCommand();
		} else if (alterExtractor.isValid()) {
			extractor = alterExtractor;
			currentCommand = extractor.getCommand();
		} else {
			logger.fatal("Command  '" + txt + "' is not valid");

			throw new SQLException("Syntax error");
		}
	}

	public commands getCurrentCommand() throws SQLException {
		if (currentCommand == null) {
			logger.fatal("There is no command");

			throw new SQLException();
		}
		return currentCommand;
	}

	public static void main(String[] args) throws IOException {
		// String path = new File("." + File.separator + "DataBases")
		// .getCanonicalPath();
		// File f = new File(path);
		// ArrayList<String> names = new ArrayList<String>(
		// Arrays.asList(f.list()));
		// names.remove("bin");
		// System.out.println(names);
		String tmp = System.getProperty("java.io.tmpdir");
		Path p = Paths.get(tmp + "/jdbc/" + 151);
		Controller control = new Controller(p, WriterType.ALTDB);
		input = new Scanner(System.in);
		System.out.println("Welcome, To stop the program write quit");
		System.out.print(">>");
		String txt = input.nextLine();
		while (!txt.equalsIgnoreCase("quit")) {
			try {
				control.setQuery(txt);
				control.excuteQuery();
				System.out.print(">>");
				txt = input.nextLine();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.print(">>");
				txt = input.nextLine();
			}
		}
	}

}