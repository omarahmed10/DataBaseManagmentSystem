package validator;

import java.util.regex.Pattern;

public class SelectValidator extends Validator {
	public SelectValidator() {
		final String select = "(?i)select";
		final String distinct = "(?i)distinct";

		regex = zeroOrMoreSpace + select + oneOrMoreSpace + "(" + distinct
				+ oneOrMoreSpace + ")" + zeroOrOne + "(" + all + or
				+ columnsCommas + ")" + oneOrMoreSpace + from + oneOrMoreSpace
				+ tableName + "("
				+ (zeroOrMoreSpace + where + oneOrMoreSpace + condition) + ")"
				+ zeroOrOne + zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}
