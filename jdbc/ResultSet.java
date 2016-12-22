package jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbmsController.DbmsController;

public class ResultSet implements java.sql.ResultSet {
	private final Logger logger;

	private int cursor;
	private boolean close;
	private List<String> selectedColumnNames;
	private List<String> selectedColumnTypes;
	private List<List<String>> selectedRowsValues;
	private Statement parentStatement;
	private ResultSetMetaData resultSetMetaData;
	private String tableName;

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public List<String> getColumnNames() {
		return selectedColumnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.selectedColumnNames = columnNames;
	}

	public List<String> getColumnTypes() {
		return selectedColumnTypes;
	}

	public void setColumnTypes(List<String> columnTypes) {
		this.selectedColumnTypes = columnTypes;
	}

	public List<List<String>> getRowsValues() {
		return selectedRowsValues;
	}

	public void setRowsValues(List<List<String>> rowsValues) {
		this.selectedRowsValues = rowsValues;
	}

	private void setClose(boolean close) {
		this.close = close;
	}

	/**
	 * Constructor for getting data from Statement.
	 * 
	 * @param columnNames
	 *            list of columns' names
	 * @param columnTypes
	 *            list of columns' types
	 * @param rowsValues
	 *            list of lists of values
	 * @param tableName
	 *            where columns belong
	 * @param parentStatement
	 *            Statement that created this ResultSet
	 */
	public ResultSet(List<String> columnNames, List<String> columnTypes,
			List<List<String>> rowsValues, String tableName,
			Statement parentStatement) {
		this.logger = LogManager.getLogger();

		this.selectedColumnNames = columnNames;
		this.selectedColumnTypes = columnTypes;
		this.selectedRowsValues = rowsValues;
		this.setCursor(0);
		this.setClose(false);
		this.parentStatement = parentStatement;
		this.tableName = tableName;
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		checkCloseState();

		if (row > 0 && row <= getRowsValues().size()) {
			this.setCursor(row);
			return true;
		} else if (row < 0 && Math.abs(row) <= getRowsValues().size()) {
			this.setCursor(getRowsValues().size() + row + 1);
			return true;
		}
		return false;
	}

	@Override
	public void afterLast() throws SQLException {
		checkCloseState();

		// This method has no effect if the result set contains no rows.
		if (!selectedRowsValues.isEmpty()) {
			this.setCursor(selectedRowsValues.size() + 1);
		}

	}

	@Override
	public void beforeFirst() throws SQLException {
		checkCloseState();
		// This method has no effect if the result set contains no rows.
		if (!selectedRowsValues.isEmpty()) {
			this.setCursor(0);
		}
	}

	@Override
	public void close() throws SQLException {
		// Close a closed ResultSet has no effect
		logger.debug("ResultSet is being closed");

		this.setClose(true);
	}

	private void checkCloseState() throws SQLException {
		if (isClosed()) {
			logger.fatal("Trying access a closed ResultSet");
			throw new SQLException();
		}
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		boolean found = false;
		int index = 0;
		// ArrayLists have a super method is called ====> contains
		if (!this.isClosed() && !selectedColumnNames.isEmpty()) {
			for (int i = 0; i < selectedColumnNames.size() && !found; i++) {
				if (selectedColumnNames.get(i).equalsIgnoreCase(columnLabel)) {
					found = true;
					index = i + 1;
				}
			}
		}
		if (found) {
			return index;
		} else {
			logger.fatal("Column label \"" + columnLabel + "\" is not found");
			throw new SQLException();
		}
	}

	@Override
	public boolean first() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty()) {
			this.setCursor(1);
			return true;
		}
		return false;
	}

	@Override
	public Statement getStatement() throws SQLException {
		checkCloseState();
		return parentStatement;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		checkCloseState();
		if (getCursor() == selectedRowsValues.size() + 1
				&& !selectedRowsValues.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		checkCloseState();
		if (getCursor() == 0 && !selectedRowsValues.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return close;
	}

	@Override
	public boolean isFirst() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty() && getCursor() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isLast() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty()
				&& getCursor() == selectedRowsValues.size()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean last() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty()) {
			setCursor(selectedRowsValues.size());
			return true;
		}
		return false;
	}

	@Override
	public boolean next() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty()
				&& getCursor() != selectedRowsValues.size()) {
			setCursor(getCursor() + 1);
			return true;
		}
		return false;
	}

	@Override
	public boolean previous() throws SQLException {
		checkCloseState();
		if (!selectedRowsValues.isEmpty() && getCursor() != 1) {
			setCursor(getCursor() - 1);
			return true;
		}
		return false;
	}

	private Object getData(int columnIndex, String dataType)
			throws SQLException {
		if (isClosed() || columnIndex > selectedColumnNames.size()
				|| columnIndex <= 0) {
			logger.fatal("ColumnIndex is not valid");
			throw new SQLException("the columnIndex is not valid");
		}
		if (!selectedColumnTypes.get(columnIndex - 1).equalsIgnoreCase(dataType)
				&& !dataType.equals("object")) {
			logger.fatal("Type of column at index " + columnIndex
					+ " isn't equal " + dataType);
			throw new SQLException();
		}
		Object type = selectedRowsValues.get(getCursor() - 1)
				.get(columnIndex - 1);
		if (type.equals("null")) {
			return null;
		}
		return type;
	}

	private Object getData(String columnLabel, String dataType)
			throws SQLException {
		checkCloseState();
		boolean found = false;
		int index = -1;
		for (int i = 0; i < selectedColumnNames.size(); i++) {
			if (selectedColumnNames.get(i).equalsIgnoreCase(columnLabel)
					&& (selectedColumnTypes.get(i).equalsIgnoreCase(dataType)
							|| dataType.equals("object"))) {
				found = true;
				index = i;
				break;
			}
		}
		if (found) {
			Object object = selectedRowsValues.get(getCursor() - 1).get(index);
			if (object.equals("null")) {
				return null;
			} else {
				return object;
			}
		} else {
			logger.fatal("Column label \"" + columnLabel + "\" is not found");
			throw new SQLException();
		}
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		Object result = getData(columnIndex, "int");
		if (result == null) {
			return 0;
		}
		return Integer.parseInt(result.toString());
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		Object result = getData(columnLabel, "int");
		if (result == null) {
			return 0;
		}
		return Integer.parseInt(result.toString());
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		Object result = getData(columnLabel, "date");
		if (result == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date answer = new Date(df.parse(result.toString()).getTime());
			return answer;
		} catch (ParseException e) {
			throw new SQLException();
		}
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		Object result = getData(columnIndex, "date");
		if (result == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date answer = new Date(df.parse(result.toString()).getTime());
			return answer;
		} catch (ParseException e) {
			throw new SQLException();
		}
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		Object result = getData(columnIndex, "varchar");
		if (result == null) {
			return null;
		}
		return result.toString();
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		Object result = getData(columnLabel, "varchar");
		if (result == null) {
			return null;
		}
		return result.toString();
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		Object result = getData(columnIndex, "float");
		if (result == null) {
			return 0;
		}
		return Float.parseFloat(result.toString());
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		Object result = getData(columnLabel, "float");
		if (result == null) {
			return 0;
		}
		return Float.parseFloat(result.toString());
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		checkCloseState();

		this.resultSetMetaData = new jdbc.ResultSetMetaData(selectedColumnNames,
				selectedColumnTypes, tableName);
		return this.resultSetMetaData;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		String data = getData(columnIndex, "object").toString();

		try {
			return Integer.parseInt(data);
		} catch (Exception exception) {
			// nothing to do
		}
		try {
			return Float.parseFloat(data);
		} catch (Exception exception) {
			// nothing to do
		}
		try {
			return Date.valueOf(data);
		} catch (Exception exception) {
			return data;
		}
	}

	// Important non-required methods
	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean wasNull() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public String getCursorName() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getType() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getConcurrency() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean rowInserted() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBoolean(String columnLabel, boolean xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBoolean(int columnIndex, boolean xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateByte(int columnIndex, byte xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateByte(String columnLabel, byte xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateShort(String columnLabel, short xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateShort(int columnIndex, short xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateInt(String columnLabel, int xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateInt(int columnIndex, int xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateLong(String columnLabel, long xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateLong(int columnIndex, long xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateFloat(String columnLabel, float xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateFloat(int columnIndex, float xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateDouble(String columnLabel, double xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateDouble(int columnIndex, double xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateString(String columnLabel, String xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateString(int columnIndex, String xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBytes(String columnLabel, byte[] xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBytes(int columnIndex, byte[] xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateDate(String columnLabel, Date xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateDate(int columnIndex, Date xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateTime(int columnIndex, Time xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateTime(String columnLabel, Time xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream xx,
			int length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream xx, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream xx, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream xx,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream xx,
			int length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream xx, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream xx, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream xx,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader xx, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader xx, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateObject(String columnLabel, Object xx, int scaleOrLength)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateObject(int columnIndex, Object xx, int scaleOrLength)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateObject(int columnIndex, Object xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateObject(String columnLabel, Object xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void refreshRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateRef(int columnIndex, Ref xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateRef(String columnLabel, Ref xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(int columnIndex, Blob xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(String columnLabel, Blob xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(int columnIndex, Clob xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(String columnLabel, Clob xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateArray(int columnIndex, Array xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateArray(String columnLabel, Array xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateRowId(int columnIndex, RowId xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateRowId(String columnLabel, RowId xx) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNString(int columnIndex, String string)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNString(String columnLabel, String string)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(String columnLabel, NClob clob)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader xx, long length)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader xx)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		throw new UnsupportedOperationException("This method is not supported");
	}

}