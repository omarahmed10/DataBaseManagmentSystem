package jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;        
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fileManager.WriterType;


public class Connection implements java.sql.Connection {
  private final Logger logger;

  private Properties info;
  private String url;
  private Statement statement;
  private boolean closed;

  /** Constructor for getting url, info, protocol from driver.
   * @param url database's url
   * @param info database's info
   * @param protocol Writer's type
   */
  public Connection(String url, Properties info, WriterType protocol) {
    this.logger = LogManager.getLogger();

    this.url = url;
    this.info = info;
    this.closed = false;
    this.statement = new Statement(info, protocol, this);
  }

  @Override
  public void close() throws SQLException {
    logger.debug("Connection is being closed");
    statement.close();
    closed = true;
  }

  @Override
  public Statement createStatement() throws SQLException {
    if (isClosed()) {
      logger.fatal("Trying access a closed Connection");
      throw new SQLException();
    }
    logger.debug("Statement is being created");
    statement.open();
    return statement;
  }
  
  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  // Important non-required methods

  @Override
  public boolean isClosed() throws SQLException {
    return closed;
  }

  public Properties getInfo() {
    return info;
  }

  public String getUrl() {
    return url;
  }

  // Unsupported methods throws java.lang.UnsupportedOperationException

  @Override
  public <T> T unwrap(Class<T> arg0) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void clearWarnings() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isWrapperFor(Class<?> arg0) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void commit() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Blob createBlob() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Clob createClob() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public NClob createNClob() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getCatalog() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getHoldability() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType,
      int resultSetConcurrency) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void rollback() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void abort(Executor arg0) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public String getSchema() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setSchema(String arg0) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }
}
