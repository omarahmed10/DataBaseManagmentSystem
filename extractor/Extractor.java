package extractor;

import java.util.List;

public interface Extractor {
	public enum commands {
		SELECT, SELECTDISTINCT, ALTERDROP, ALTERADD, DROP, USE, DELETE, CREATEDB,CREATETABLE, INSERT, UPDATE
	}

	public boolean isValid();

	public commands getCommand();

	public String getDatabaseName();

	public String getTableName();

	public List<String> getColumnsNames();

	public List<String> getColumnsTypes();

	public String getConditionFirstOperand();

	public String getConditionSecondOperand();

	public String getConditionOperator();

	public List<String> getValues();
}
