package extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.InsertValidator;
import validator.Validator;

public class InsertExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public InsertExtractor(String statement) {	
		this.validator = new InsertValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		return commands.INSERT;
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
		if (matcher.group(3) == null)
			return null;

		String columns = matcher.group(3);
		
		columns = columns.replaceAll("\\s", "");
		
		return Arrays.asList(columns.split(","));
	}
	
	@Override
	public ArrayList<String> getColumnsTypes() {
		return null;
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
	public List<String> getValues() {
		String values = matcher.group(7);
		values = values.replaceAll("\\s*'\\s*|\\(|\\)", "");
		values = values.replaceAll("\\s*,\\s*", ",");
		return Arrays.asList(values.split(","));
	}

}
