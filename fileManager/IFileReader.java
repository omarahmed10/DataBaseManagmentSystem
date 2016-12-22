package fileManager;

import dbms.MiniTable;

public interface IFileReader {
	public MiniTable loadTable(String tName, String dbName);
}
