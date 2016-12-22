package validator;

import java.util.regex.Pattern;

public class CreateValidator extends Validator {

	public CreateValidator() {
		final String create = "(?i)create";
		
		regex = zeroOrMoreSpace + create + oneOrMoreSpace + "((" + (database + oneOrMoreSpace + databaseName)
				+ ")" + or + "(" + (table + oneOrMoreSpace + tableName + zeroOrMoreSpace + columnsCommasTypes) + "))"
				+ zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}