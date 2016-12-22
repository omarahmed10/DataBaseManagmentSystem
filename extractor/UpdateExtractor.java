package extractor;

import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.UpdateValidator;
import validator.Validator;

public class UpdateExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public UpdateExtractor(String statement) {	
		this.validator = new UpdateValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		return commands.UPDATE;
	}

	@Override
	public String getDatabaseName() {
		return null;
	}

	@Override
	public String getTableName() {
		return matcher.group(1);
	}

	@Override
	public List<String> getColumnsNames() {
		String columns = matcher.group(2);

		columns = columns.replaceAll(
				"('[^,]*')|=\\s*([0-9]{1,15}(\\.([0-9]{1,15})?)?)|\\s|=", "");

		return Arrays.asList(columns.split(","));
	}

	@Override
	public List<String> getColumnsTypes() {
		return null;
	}

	@Override
	public String getConditionFirstOperand() {
		return matcher.group(23);
	}

	@Override
	public String getConditionSecondOperand() {
		return matcher.group(25).replaceAll("'", "");
	}

	@Override
	public String getConditionOperator() {
		return matcher.group(24);
	}

	@Override
	public List<String> getValues() {
		String values = matcher.group(2);
		// remove any thing but values , commas and quotes
		values = values.replaceAll("[a-zA-Z]+[0-9]*_?[a-zA-Z]*[0-9]*" + "\\s*=", "");
		// remove any quotes and redundant spaces
		values = values.replaceAll("\\s*'\\s*", "");

		values = values.replaceAll("\\s*,\\s*", ",");

		return Arrays.asList(values.trim().split(","));
	}
}