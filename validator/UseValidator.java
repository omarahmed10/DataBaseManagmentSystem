package validator;

import java.util.regex.Pattern;

public class UseValidator extends Validator {
	public UseValidator() {
		final String use = "(?i)use";

		regex = zeroOrMoreSpace + use + oneOrMoreSpace + databaseName
				+ zeroOrMoreSpace + semiColon + zeroOrMoreSpace;

		regexPattern = Pattern.compile(regex);
	}
}
