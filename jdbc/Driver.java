package jdbc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import fileManager.WriterType;

public class Driver implements java.sql.Driver {
  private final org.apache.logging.log4j.Logger logger;

  private WriterType protocol;

  public WriterType getProtocol() {
    return protocol;
  }

  public void setProtocol(WriterType protocol) {
    this.protocol = protocol;
  }

  public Driver() {
    this.logger = LogManager.getLogger();
  }

  /**Check whether the driver thinks that it can open a connection to the given URL.
   * @param url database's url.
   * @return boolean  
   */
  @Override
  public boolean acceptsURL(String url) throws SQLException {
    logger.debug("URL  \"" + url + "\"  is being understood");
    boolean typeSafe;
    String[] tmp = url.split(":");
    typeSafe = (tmp[1].equals("xmldb")) || (tmp[1].equals("altdb"));
    if (tmp[0].equals("jdbc") && typeSafe && tmp[2].equals("//localhost")) {
      if (tmp[1].equalsIgnoreCase("xmldb")) {
        this.setProtocol(WriterType.XMLDB);
      } else {
        this.setProtocol(WriterType.ALTDB);
      }
      return true;
    }
    return false;
  }

  // The driver should throw an SQLException if it is the right driver to
  // connect to the given URL but has trouble connecting to the database.
  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    logger.debug("URL \"" + url + "\" and Properties \"" + info
        + "\" are being connected");

    Connection connection = null;
    if (!acceptsURL(url)) {
      // The driver should return "null" if it realizes it is the wrong
      // kind of driver to connect to the given URL
      return null;
    }
    checkExistence(info);
    connection = new Connection(url, info, this.protocol);
    return connection;
  }

  private void checkExistence(Properties info) throws SQLException {
    Path path = Paths.get(info.get("path").toString());
    if (Files.notExists(path)) {
      try {
        Files.createDirectories(path);
      } catch (IOException execption) {
        // System.out.println("type1 error :D");
        throw new SQLException();
      }
    } else {
      // System.out.println("type2 error :D");
      throw new SQLException();
    }
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
      throws SQLException {

    if (!acceptsURL(url)) {
      throw new SQLException("database access error occurs");
    }
    return new DriverPropertyInfo[] { new DriverPropertyInfo("path",
        ((File) info.get("path")).getAbsolutePath()) };
  }

  // Unsupported methods throws java.lang.UnsupportedOperationException
  
  @Override
  public int getMajorVersion() {
    throw new UnsupportedOperationException("Unsupported Method");
  }

  @Override
  public int getMinorVersion() {
    throw new UnsupportedOperationException("Unsupported Method");
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new UnsupportedOperationException("Unsupported Method");
  }

  @Override
  public boolean jdbcCompliant() {
    throw new UnsupportedOperationException("Unsupported Method");
  }
}
