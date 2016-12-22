package validator;

import java.util.regex.Pattern;

public class UpdateValidator extends Validator {
	public UpdateValidator() {
		final String update = "(?i)update";

		regex = zeroOrMoreSpace + update + oneOrMoreSpace + tableName + oneOrMoreSpace + set + oneOrMoreSpace
				+ columnsValuesCommas + "(" + (zeroOrMoreSpace + where + oneOrMoreSpace + condition) + ")" + zeroOrOne
				+ zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}