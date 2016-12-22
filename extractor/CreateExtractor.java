package extractor;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.CreateValidator;
import validator.Validator;

public class CreateExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public CreateExtractor(String statement) {
		this.validator = new CreateValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		if (getDatabaseName() != null) {
			return commands.CREATEDB;
		}
		return commands.CREATETABLE;
	}

	@Override
	public String getDatabaseName() {
		return matcher.group(3);
	}

	@Override
	public String getTableName() {
		return matcher.group(5);
	}

	@Override
	public List<String> getColumnsNames() {
		String columns = matcher.group(6);
		if (columns == null)
			return null;
		columns = columns.replaceAll("\\s(?i)varchar\\s*,|\\s(?i)int\\s*,|\\s(?i)date\\s*,|\\s(?i)float\\s*,", ",");
		columns = columns.replaceAll("\\s(?i)varchar\\s*\\)|\\s(?i)int\\s*\\)|\\s(?i)date\\s*\\)|\\s(?i)float\\s*\\)",
				"");
		columns = columns.replaceAll("\\s|\\(", "");
		return Arrays.asList(columns.split(","));
	}

	@Override
	public List<String> getColumnsTypes() {
		String types = matcher.group(6);
		List<String> columns = getColumnsNames();

		for (int i = 0; i < columns.size(); i++) {
			types = types.replace(columns.get(i), "");
		}

		types = types.replaceAll("\\s|\\(|\\)", "");

		return Arrays.asList(types.split(","));
	}

	@Override
	public String getConditionFirstOperand() {
		return null;
	}

	@Override
	public String getConditionSecondOperand() {
		return null;
	}

	@Override
	public String getConditionOperator() {
		return null;
	}

	@Override
	public ArrayList<String> getValues() {
		return null;
	}
}