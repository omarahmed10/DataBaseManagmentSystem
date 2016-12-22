package extractor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.DeleteValidator;
import validator.Validator;

public class DeleteExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public DeleteExtractor(String statement) {
		this.validator = new DeleteValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		return commands.DELETE;
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
		return null;
	}

	@Override
	public List<String> getColumnsTypes() {
		return null;
	}

	@Override
	public String getConditionFirstOperand() {
		return matcher.group(3);
	}

	@Override
	public String getConditionSecondOperand() {
		return matcher.group(5).replaceAll("'", "");
	}

	@Override
	public String getConditionOperator() {
		return matcher.group(4);
	}

	@Override
	public List<String> getValues() {
		return null;
	}
}