package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jdbc.ResultSet;
import jdbc.ResultSetMetaData;

public class MetaDataTest {
	String[] s1 = { "ID", "Name", "School", "Age", "Grades", "GraduationDate" };
	String[] s2 = { "int", "varchar", "varchar", "int", "float", "date" };
	List<String> columnsNames = Arrays.asList(s1);
	List<String> columnsTypes = Arrays.asList(s2);
	String tableName = "Students";

	ResultSetMetaData metaData = new ResultSetMetaData(columnsNames,
			columnsTypes, tableName);

	@Test
	public void testColumnsNumber() throws SQLException {

		assertEquals(6, metaData.getColumnCount());

	}

	@Test
	public void testColumnsNames() throws SQLException {

		assertEquals("ID", metaData.getColumnName(1));
		assertEquals("Name", metaData.getColumnName(2));
		assertEquals("School", metaData.getColumnName(3));
		assertEquals("Age", metaData.getColumnName(4));
		assertEquals("Grades", metaData.getColumnName(5));
		assertEquals("GraduationDate", metaData.getColumnName(6));

	}

	@Test
	public void testColumnsLabels() throws SQLException {

		assertEquals("ID", metaData.getColumnLabel(1));
		assertEquals("Name", metaData.getColumnLabel(2));
		assertEquals("School", metaData.getColumnLabel(3));
		assertEquals("Age", metaData.getColumnLabel(4));
		assertEquals("Grades", metaData.getColumnLabel(5));
		assertEquals("GraduationDate", metaData.getColumnLabel(6));

	}

	@Test
	public void testColumnsTypes() throws SQLException {

		assertEquals(Types.INTEGER, metaData.getColumnType(1));
		assertEquals(Types.VARCHAR, metaData.getColumnType(2));
		assertEquals(Types.VARCHAR, metaData.getColumnType(3));
		assertEquals(Types.INTEGER, metaData.getColumnType(4));
		assertEquals(Types.FLOAT, metaData.getColumnType(5));
		assertEquals(Types.DATE, metaData.getColumnType(6));

	}

	@Test
	public void testTableName() throws SQLException {

		assertEquals("Students", metaData.getTableName(1));
		assertEquals("Students", metaData.getTableName(2));
		assertEquals("Students", metaData.getTableName(3));
		assertEquals("Students", metaData.getTableName(4));
		assertEquals("Students", metaData.getTableName(5));
		assertEquals("Students", metaData.getTableName(6));

	}

	@Test
	public void testUnsupportedMethods() throws SQLException {
		boolean isThrown = true;

		try {
			metaData.getCatalogName(1);
			isThrown = false;
		} catch (Exception e) {
			assertTrue(isThrown);
		}

		try {
			metaData.isCurrency(1);
			isThrown = false;
		} catch (Exception e) {
			assertTrue(isThrown);
		}

		try {
			metaData.unwrap(ResultSet.class);
			isThrown = false;
		} catch (Exception e) {
			assertTrue(isThrown);
		}

	}

}
