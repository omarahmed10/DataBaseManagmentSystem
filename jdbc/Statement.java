package jdbc;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbmsController.DbmsController;
import extractor.Extractor.commands;
import fileManager.WriterType;

public class Statement implements java.sql.Statement {
  private final Logger logger;

  private Connection parentConnection;
  private ArrayList<String> batch;
  private boolean closed;
  private ResultSet resultSet;
  private int updateCount;
  private DbmsController sqlController;

  /**Constructor for getting data from connection.
   * @param info database info
   * @param protocol writer type
   * @param parentConnection Connection that created this Statement
   */
  public Statement(Properties info, WriterType protocol, Connection parentConnection) {
    this.logger = LogManager.getLogger();

    this.parentConnection = parentConnection;
    this.batch = new ArrayList<String>();
    this.closed = false;
    this.updateCount = -1;
    this.sqlController = new DbmsController(Paths.get(info.get("path").toString()), protocol);
  }

  @Override
  public void addBatch(String sqlStatement) throws SQLException {
    checkCloseState();
    batch.add(sqlStatement);
  }

  @Override
  public void clearBatch() throws SQLException {
    checkCloseState();
    batch.clear();
  }

  private void checkCloseState() throws SQLException {
    if (isClosed()) {
      logger.fatal("Trying access a closed Statement");
      throw new SQLException();
    }
  }

  public void open() {
    closed = false;
  }

  @Override
  public void close() throws SQLException {
    logger.debug("Statement is being closed");
    closed = true;
  }

  // add functionality >> if you perform two select commands you should close
  // the prev. result set.
  @Override
  public boolean execute(String command) throws SQLException {
    logger.info("Command  '" + command + "' entered execution process");

    checkCloseState();
    sqlController.setStatement(command);
    // this may not work well so i have changed it look at the main.
    boolean hasResultSet = false;
    commands requiredQuery = sqlController.getQueryType();
    if (requiredQuery == commands.SELECT || requiredQuery == commands.SELECTDISTINCT) {
      hasResultSet = true;
    }
    sqlController.excuteStatment();
    if (hasResultSet) {
      if (resultSet != null) {
        resultSet.close();
      }
      if (sqlController.getSelectedData() == null || sqlController
          .getSelectedData().size() == 0) {
        hasResultSet = false;
      } else {
        resultSet = new jdbc.ResultSet(sqlController.getColumnsName(),
            sqlController.getDataType(), sqlController.getSelectedData(),
            sqlController.getTableName(), this);
      }
      updateCount = -1;
    } else {
      updateCount = sqlController.getAffectedRowsNumber();
      resultSet = null;
    }

    logger.info("Command  '" + command + "' executed successfully");
    
    return hasResultSet;
  }
  
  @Override
  public boolean execute(String paramString, int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }
  
  @Override
  public boolean execute(String paramString, int[] paramArrayOfInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  // there is issues concerning failure or fault of a command
  // and number of affected rows of statements like drop , select
  @Override
  public int[] executeBatch() throws SQLException {
    logger.info("Batch entered execution process");

    checkCloseState();
    int[] updateCounts = new int[batch.size()];
    for (int i = 0; i < batch.size(); i++) {
      try {
        sqlController.setStatement(batch.get(i));
        boolean hasResultSet = false;
        commands requiredQuery = sqlController.getQueryType();
        if (requiredQuery == commands.SELECT || requiredQuery 
            == commands.SELECTDISTINCT) { 
          hasResultSet = true;
        }
        sqlController.excuteStatment();
        // if sql command has a ResultSet , its updateCounts = -2 ,
        // otherwise updateCounts > 0
        if (hasResultSet) {
          updateCounts[i] = SUCCESS_NO_INFO;
        } else {
          updateCounts[i] = sqlController.getAffectedRowsNumber();
        }
      } catch (Exception exception) {
        logger.error("Execution failed");
        updateCounts[i] = EXECUTE_FAILED;
      }
    }
    
    logger.info("Batch executed successfully");
    
    return updateCounts;
  }

  @Override
  public ResultSet executeQuery(String command) throws SQLException {
    logger.info("Query '" + command + "' entered execution process");

    checkCloseState();

    sqlController.setStatement(command);
    commands requiredQuery = sqlController.getQueryType();

    if (requiredQuery != commands.SELECT && requiredQuery
        != commands.SELECTDISTINCT) {
      logger.fatal("Command type \"" + requiredQuery + "\" hasn't a ResultSet");
      throw new SQLException();
    }
    sqlController.excuteStatment();
    if (resultSet != null) {
      resultSet.close();
    }
    resultSet = new jdbc.ResultSet(sqlController.getColumnsName(),
        sqlController.getDataType(), sqlController.getSelectedData(),
        sqlController.getTableName(), this);

    // updateCount should be -1 if current result have resultSet
    updateCount = -1;
    
    logger.info("Query  '" + command + "' executed successfully");

    return resultSet;
  }

  @Override
  public int executeUpdate(String command) throws SQLException {
    logger.info("Update command  '" + command + "' entered execution process");
    
    checkCloseState();
    sqlController.setStatement(command);
    commands requiredQuery = sqlController.getQueryType();
    if (requiredQuery == commands.SELECT && requiredQuery == commands.SELECTDISTINCT) {
      logger.fatal("Command type \"" + requiredQuery + "\" hasn't update count rows");
      throw new SQLException();
    }
    sqlController.excuteStatment();
    updateCount = sqlController.getAffectedRowsNumber();
    
    logger.info("Update command  '" + command + "' executed successfully");
    
    return updateCount;
  }
  
  @Override
  public int executeUpdate(String paramString, int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }
  
  @Override
  public int executeUpdate(String paramString, int[] paramArrayOfInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public Connection getConnection() throws SQLException {
    checkCloseState();
    return parentConnection;
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    checkCloseState();
    return resultSet;
  }

  @Override
  public int getUpdateCount() throws SQLException {
    checkCloseState();
    return updateCount;
  }

  @Override
  public boolean isClosed() throws SQLException {
    return closed;
  }

  // Unsupported methods throws java.lang.UnsupportedOperationException
  
  @Override
  public int getQueryTimeout() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setQueryTimeout(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }
  
  @Override
  public int getMaxFieldSize() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setMaxFieldSize(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getMaxRows() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setMaxRows(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void cancel() throws SQLException {
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
  public void setCursorName(String paramString) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }
  
  @Override
  public boolean getMoreResults(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setFetchDirection(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getFetchDirection() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setFetchSize(int paramInt) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getFetchSize() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getResultSetType() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void setPoolable(boolean paramBoolean) throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isPoolable() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public void closeOnCompletion() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    throw new UnsupportedOperationException("This method is not supported");
  }

}