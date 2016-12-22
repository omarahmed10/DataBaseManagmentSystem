package extractor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.UseValidator;
import validator.Validator;

public class UseExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public UseExtractor(String statement) {	
		this.validator = new UseValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		return commands.USE;
	}

	@Override
	public String getDatabaseName() {
		return matcher.group(1);
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public ArrayList<String> getColumnsNames() {
		return null;
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
	public ArrayList<String> getValues() {
		return null;
	}
}