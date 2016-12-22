package validator;

import java.util.regex.Pattern;

public class AlterValidator extends Validator {
	
	public AlterValidator() {	
		final String alter = "(?i)alter";
		final String table = "(?i)table";
		final String column = "(?i)column";
		final String add = "(?i)add";
		final String drop = "(?i)drop";

		regex = zeroOrMoreSpace + alter + oneOrMoreSpace + table
				+ oneOrMoreSpace + tableName + oneOrMoreSpace + "((" + "(" + add
				+ ")" + oneOrMoreSpace + "(" + columnName + ")" + oneOrMoreSpace
				+ dataType + ")" + or + "(" + "(" + drop + ")" + oneOrMoreSpace
				+ column + oneOrMoreSpace + "(" + columnName + ")" + "))"
				+ zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
	
}
