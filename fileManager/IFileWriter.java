package fileManager;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import dbms.Column;
import dbms.MiniTable;

public interface IFileWriter {
	public void write(String tName, Path dbPath, List<Column> cols,
			MiniTable requiredTable) throws SQLException;

}
