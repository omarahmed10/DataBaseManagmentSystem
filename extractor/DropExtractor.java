package extractor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.DropValidator;
import validator.Validator;

public class DropExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;
	
	// Constructor
	public DropExtractor(String statement) {		
		this.validator = new DropValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}
	
	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		return commands.DROP;
	}

	@Override
	public String getDatabaseName() {
		return matcher.group(2);
	}

	@Override
	public String getTableName() {
		return matcher.group(3);
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
		return null;
	}
}