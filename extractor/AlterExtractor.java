package extractor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import validator.AlterValidator;
import validator.Validator;

public class AlterExtractor implements Extractor {
	private Validator validator;

	private Pattern regexPattern;

	private Matcher matcher;

	// Constructor
	public AlterExtractor(String statement) {
		this.validator = new AlterValidator();
		this.regexPattern = validator.getPattern();
		this.matcher = regexPattern.matcher(statement);
	}

	@Override
	public boolean isValid() {
		return matcher.matches();
	}

	@Override
	public commands getCommand() {
		if (matcher.group(4) != null)
			return commands.ALTERADD;

		return commands.ALTERDROP;
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
		String columnName;
		if (matcher.group(5) != null)
			columnName = matcher.group(5);
		else
			columnName = matcher.group(9);

		return Arrays.asList(new String[] { columnName });
	}

	@Override
	public List<String> getColumnsTypes() {
		if (matcher.group(6) == null)
			return null;

		return Arrays.asList(matcher.group(6));
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
