package validator;

import java.util.regex.Pattern;

public class DeleteValidator extends Validator {
	public DeleteValidator() {
		final String delete = "(?i)delete";

		regex = zeroOrMoreSpace + delete + oneOrMoreSpace + from + oneOrMoreSpace + tableName
				+ "(" + (zeroOrMoreSpace + where + oneOrMoreSpace + condition) + ")" + zeroOrOne
				+ zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}
