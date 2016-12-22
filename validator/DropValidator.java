package validator;

import java.util.regex.Pattern;

public class DropValidator extends Validator {
	public DropValidator() {
		final String drop = "(?i)drop";

		regex = zeroOrMoreSpace + drop + oneOrMoreSpace + "(" + (database + oneOrMoreSpace + databaseName)
				+ or + (table + oneOrMoreSpace + tableName) + ")" + zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}
