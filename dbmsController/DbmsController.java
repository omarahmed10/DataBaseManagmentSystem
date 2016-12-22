package dbmsController;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extractor.Extractor.commands;
import fileManager.WriterType;

/**
 * this is a temporary class.
 *
 * @author omar
 *
 */
public class DbmsController {
	private final Logger logger;
	private Controller control;
	public DbmsController(Path dbPath, WriterType writerType) {
		logger = LogManager.getLogger();
		// System.out.println(dbPath.getFileName());
		control = new Controller(dbPath, writerType);
	}

	public void setStatement(String txt) throws SQLException {
		logger.debug("Command  '" + txt + "' is being validated");
		control.setQuery(txt);
	}

	public void excuteStatment() throws SQLException {
		logger.debug("Command is being executed");
		control.excuteQuery();
	}

	public commands getQueryType() throws SQLException{
		return control.getCurrentCommand();
	}
	public int getAffectedRowsNumber() {
		return control.getEffectedRowsNumber();
	}

	public List<List<String>> getSelectedData() throws SQLException {
		return control.getSelectedDataValues();
	}


	public List<String> getDataType() {
		return control.getSelectedColumnDataType();
	}

	public List<String> getColumnsName() {
		return control.getSelectedColumnNames();
	}

	public String getTableName() {
		return control.getTableName();
	}
}