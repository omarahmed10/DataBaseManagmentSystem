package condition;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbms.Column;
import dbms.DataBaseMS;

public class ConditionFinder {
	private final Logger logger = LogManager.getLogger();
	
	private String firstOperand, secondOperand, tableName;
	private DataBaseMS dbms;
	private List<Integer> rowsNumber;
	private int operator;

	/**
	 * mask is an String used to get the row at which the condition has matched.
	 * Example: if the matched rows are (0,1,2,3,4) in a column of 6 elements
	 * then mask = "011111".
	 */
	// private int mask;

	public ConditionFinder(String firstOperand, String operator,
			String secondOperand, DataBaseMS dbms, String tableName)
			throws SQLException {
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.dbms = dbms;
		this.tableName = tableName;
		switch (operator) {
		case "=":
			this.operator = 0;
			break;
		case ">":
			this.operator = 1;
			break;
		case "<":
			this.operator = -1;
			break;

		default:
			break;
		}
		// this.mask = 0;
		excuteCondition();
	}

	// public int getMask() {
	// return mask;
	// }

	public List<Integer> getRowsNumber() {
		return rowsNumber;
	}

	private void excuteCondition() throws SQLException {
		logger.debug("Where condirion is being checked and executed");
		
		List<Column> tableData = dbms.selectFromTable(this.tableName, null);
		for (int i = 0; i < tableData.size(); i++) {
			if (tableData.get(i).getName().equalsIgnoreCase(firstOperand)) {
				rowsNumber = new ArrayList<>();
				ArrayList<String> rows = tableData.get(i).getColElements();
				//System.out.println("rooooooooooooooows    " + rows);
				for (int j = 0; j < rows.size(); j++) {
					if (tableData.get(i).getType().equalsIgnoreCase("int")
							&& compareIntegers(rows.get(j))) {
						rowsNumber.add(j);
					} else if (tableData.get(i).getType().equalsIgnoreCase(
							"Varchar") && compareStrings(rows.get(j))) {
						rowsNumber.add(j);
					} else if (tableData.get(i).getType().equalsIgnoreCase(
							"float") && compareFloats(rows.get(j))) {
						rowsNumber.add(j);
					} else if (tableData.get(i).getType().equalsIgnoreCase(
							"date") && compareDates(rows.get(j))) {
						rowsNumber.add(j);
					}
				}
			}
		}
		//System.out.println("condition rows : " + rowsNumber + " of column: "
//				+ firstOperand);
	}

	private boolean compareIntegers(String element) throws SQLException {
		try {
			Integer operand = Integer.parseInt(element);
			Integer operand2 = Integer.parseInt(secondOperand);
			int result = operand.compareTo(operand2);
			return (result < 0 && this.operator < 0)
					|| (result > 0 && this.operator > 0)
					|| (result == 0 && this.operator == 0);
		} catch (Exception e) {
			logger.fatal("Types aren't matched in where condition");
			throw new SQLException("Type NOT matched Error.");
		}
	}

	private boolean compareStrings(String element) throws SQLException {
		try {
			int result = element.compareTo(secondOperand);
			return (result < 0 && this.operator < 0)
					|| (result > 0 && this.operator > 0)
					|| (result == 0 && this.operator == 0);
		} catch (Exception e) {
			logger.fatal("Types aren't matched in where condition");
			throw new SQLException("Type NOT matched Error.");
		}
	}

	private boolean compareFloats(String element) throws SQLException {
		try {
			Float operand = Float.parseFloat(element);
			Float operand2 = Float.parseFloat(this.secondOperand);
			int result = operand.compareTo(operand2);
			return (result < 0 && this.operator < 0)
					|| (result > 0 && this.operator > 0)
					|| (result == 0 && this.operator == 0);
		} catch (Exception e) {
			logger.fatal("Types aren't matched in where condition");
			throw new SQLException("Type NOT matched Error.");
		}
	}

	private boolean compareDates(String element) throws SQLException {
		try {
			Date operand = Date.valueOf(element);
			Date operand2 = Date.valueOf(this.secondOperand);
			int result = operand.compareTo(operand2);
			return (result < 0 && this.operator < 0)
					|| (result > 0 && this.operator > 0)
					|| (result == 0 && this.operator == 0);
		} catch (Exception e) {
			logger.fatal("Types aren't matched in where condition");
			throw new SQLException("Type NOT matched Error.");
		}
	}
	// private boolean compare(String comparisonOP, String element, Object
	// Value) {
	// try {
	// int operand = Integer.parseInt(Value.toString());
	// if (comparisonOP.contains("<")) {
	// // System.out.println(Integer.parseInt(element) < operand);
	// return Integer.parseInt(element) < operand;
	// } else if (comparisonOP.contains(">")) {
	// return Integer.parseInt(element) > operand;
	// } else {
	// return false;
	// }
	// } catch (Exception e) {
	// return false;
	// }
	// }

	// private void generateRowNumbers() {
	// rowsNumber = new ArrayList<Integer>();
	// int index = 1;
	// while(mask != 0){
	// if(mask % (Math.pow(10, index)) == 1){
	//
	// }
	// index++;
	// }
	// }
}
