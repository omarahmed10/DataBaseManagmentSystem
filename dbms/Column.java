package dbms;

import java.util.ArrayList;

public class Column {
	/**
	 * column name.
	 */
	private String name;
	/**
	 * column data type.
	 */
	private String type;
	/**
	 * column Elements that appear as Column values in each row
	 */
	private ArrayList<String> colElements;

	public Column() {
		name = new String();
		type = new String();
		colElements = new ArrayList<String>();
	}

	/**
	 * @return the name of Column Object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param n
	 *            the name of Column to set
	 */
	public void setName(String n) {
		this.name = n;
	}

	/**
	 * @return the type of this Column
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param t
	 *            the type of Column to set
	 */
	public void setType(String t) {
		this.type = t;
	}

	/**
	 * @return the column Elements
	 */
	public ArrayList<String> getColElements() {
		return colElements;
	}

	/**
	 * @param elements
	 *            the column Elements to set
	 */
	public void setColElements(ArrayList<String> elements) {
		this.colElements = elements;
	}

	public Column deepCopy() {
		Column newCopy = new Column();
		String n = this.getName();
		String t = this.getType();
		ArrayList<String> elements = new ArrayList<String>(
				this.getColElements());
		newCopy.setName(n);
		newCopy.setType(t);
		newCopy.setColElements(elements);
		return newCopy;
	}
}