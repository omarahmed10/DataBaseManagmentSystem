package jdbc;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ResultSetMetaData implements java.sql.ResultSetMetaData {
  private List<String> columnsNames;
  private List<String> columnsTypes;
  private String tableName;
  // All indices are decremented by one , this is the difference one
  private final int indexDifferrence;
  
  /**Constructor for getting required data from Statement.
   * @param columnsNames list of columns' names
   * @param columnsTypes list of columns' types
   * @param tableName where columns belong
   */
  public ResultSetMetaData(List<String> columnsNames ,
      List<String> columnsTypes , String tableName) {
    this.columnsNames = columnsNames;
    this.columnsTypes = columnsTypes;
    this.tableName = tableName;
    this.indexDifferrence = 1;
  }

  @Override
  public int getColumnCount() throws SQLException {
    return columnsNames.size();
  }

  @Override
  public String getColumnLabel(int columnIndex) throws SQLException {
    return columnsNames.get(columnIndex - indexDifferrence);
  }

  @Override
  public String getColumnName(int columnIndex) throws SQLException {
    return columnsNames.get(columnIndex - indexDifferrence);
  }

  @Override
  public int getColumnType(int columnIndex) throws SQLException {
    return getIntType(columnsTypes.get(columnIndex - indexDifferrence));
  }

  @Override
  public String getTableName(int columnIndex) throws SQLException {
    return tableName;
  }

  private int getIntType(String stringType) {
    if (stringType.toLowerCase().equals("varchar")) {
      return Types.VARCHAR;
    }
    if (stringType.toLowerCase().equals("int")) {
      return Types.INTEGER;
    }
    if (stringType.toLowerCase().equals("float")) {
      return Types.FLOAT;
    } else {
      return Types.DATE;
    }
  }

  // Unsupported methods throws java.lang.UnsupportedOperationException

  @Override
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isAutoIncrement(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isCaseSensitive(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isSearchable(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isCurrency(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int isNullable(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isSigned(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getColumnDisplaySize(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getSchemaName(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getPrecision(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getScale(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getCatalogName(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getColumnTypeName(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isReadOnly(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isWritable(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isDefinitelyWritable(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getColumnClassName(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

}
