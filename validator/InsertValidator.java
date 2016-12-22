package validator;

import java.util.regex.Pattern;

public class InsertValidator extends Validator {
	public InsertValidator() {
		final String insert = "(?i)insert";

		regex = zeroOrMoreSpace + insert + oneOrMoreSpace + into + oneOrMoreSpace + tableName + zeroOrMoreSpace
				+ columnsCommasBrackets + zeroOrOne + zeroOrMoreSpace + values + zeroOrMoreSpace + valuesCommasBrackets
				+ zeroOrMoreSpace + semiColon;

		regexPattern = Pattern.compile(regex);
	}
}
