package extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.SelectValidator;
import validator.Validator;

public class SelectExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public SelectExtractor(String statement) {
		this.validator = new SelectValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	public Matcher getMatcher() {
		return matcher;
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		if (matcher.group(1) != null)
			return commands.SELECTDISTINCT;

		return commands.SELECT;
	}

	@Override
	public String getDatabaseName() {
		return null;
	}

	@Override
	public String getTableName() {
		return matcher.group(7);
	}

	@Override
	public List<String> getColumnsNames() {
		String columns = matcher.group(2);

		if (columns.equals("*"))
			return null;
		columns = columns.trim();
		return Arrays.asList(columns.split("\\s*,\\s"));
	}

	@Override
	public ArrayList<String> getColumnsTypes() {
		return null;
	}

	@Override
	public String getConditionFirstOperand() {
		return matcher.group(9);
	}

	@Override
	public String getConditionSecondOperand() {
		return matcher.group(12).replaceAll("'", "");
	}

	@Override
	public String getConditionOperator() {
		return matcher.group(10);
	}

	@Override
	public ArrayList<String> getValues() {
		return null;
	}
}