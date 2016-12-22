package extractor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import extractor.Extractor.commands;

public class ExtractorTest {

	@Test
	public void test1() {
		String statement = "create table table1 (students varchar, grades int, dates date , floats float)";
		Extractor extractor = new CreateExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals("table1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionOperator());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test2() {
		String statement = "drop dataBasE database;";
		Extractor extractor = new DropExtractor(statement);
		assertTrue(extractor.isValid());
		assertNull(extractor.getColumnsNames());
		assertEquals("database", extractor.getDatabaseName());
		assertNull(extractor.getTableName());
		assertNull(extractor.getConditionOperator());
		assertNull(extractor.getConditionFirstOperand());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test3() {
		String statement = "insert INTO t1 values ('Ahmed' , 18.6);";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue(extractor.isValid());
		assertNull(extractor.getColumnsNames());
		assertEquals("t1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionOperator());
		assertNull(extractor.getConditionFirstOperand());
		assertEquals("Ahmed", extractor.getValues().get(0));
		assertEquals("18.6", extractor.getValues().get(1));
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test4() {
		String statement = "select * from t1 where c1 < 122. ;";
		Extractor extractor = new SelectExtractor(statement);
		assertTrue(extractor.isValid());
		assertNull(extractor.getColumnsNames());
		assertEquals("t1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertEquals("c1", extractor.getConditionFirstOperand());
		assertEquals("<", extractor.getConditionOperator());
		assertEquals("122.", extractor.getConditionSecondOperand());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test

	public void test5() {
		String statement = "select c1 , c2 from tableName;";
		Extractor extractor = new SelectExtractor(statement);
		assertTrue(extractor.isValid());
		List<String> test = new ArrayList<String>();
		test.add("c1");
		test.add("c2");
		assertEquals(test, extractor.getColumnsNames());
		assertEquals("tableName", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionOperator());
		assertNull(extractor.getConditionFirstOperand());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test6() {
		String statement = "update t1 set c1 = 15.6 , c2 = 'v2 k', c4 = 'N5';";
		UpdateExtractor extractor = new UpdateExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals("t1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionFirstOperand());
		assertNull(extractor.getConditionOperator());

		/*
		 * for (int i = 0; i < extractor.matcher.groupCount(); i++) {
		 * System.out.println(i + "  " + extractor.matcher.group(i)); }
		 */

		List<String> test = new ArrayList<String>();
		test.add("c1");
		test.add("c2");
		test.add("c4");
		assertEquals(test, extractor.getColumnsNames());
		test.clear();
		test.add("15.6");
		test.add("v2 k");
		test.add("N5");
		assertEquals(test, extractor.getValues());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test

	public void test7() {
		String statement = "update t1 set c1 = 1 , c2 = 2.5, c4 = 4;";
		Extractor extractor = new UpdateExtractor(statement);
		assertTrue(extractor.isValid());
		List<String> test = new ArrayList<String>();
		test.add("1");
		test.add("2.5");
		test.add("4");
		assertEquals(test, extractor.getValues());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test8() {
		String statement = "DELETE From table_name11 WHERE coLUmn_NAME3>'2011-01-25'";
		Extractor extractor = new DeleteExtractor(statement);
		assertTrue(extractor.isValid());
		assertNull(extractor.getColumnsNames());
		assertEquals("coLUmn_NAME3", extractor.getConditionFirstOperand());
		assertEquals(">", extractor.getConditionOperator());
		assertEquals("2011-01-25", extractor.getConditionSecondOperand());
//		 System.out.println("columnNames " + extractor.getColumnsNames());
//		 System.out.println("columnTypes " + extractor.getColumnsTypes());
//		 System.out.println("values " + extractor.getValues() + " " +
//		 extractor.getClass().getSimpleName());
	}
	//DELETE From table_name11 WHERE coLUmn_NAME3>'2011-01-25'
	@Test

	public void test9() {
		String statement = "select Distinct * from t1 where c1 < 12 ;";
		Extractor extractor = new SelectExtractor(statement);
		assertTrue(extractor.isValid());
		assertNull(extractor.getColumnsNames());
		assertEquals("t1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertEquals("c1", extractor.getConditionFirstOperand());
		assertEquals("<", extractor.getConditionOperator());
		assertEquals("12", extractor.getConditionSecondOperand());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test10() {
		String statement = "select Distinct gh_cx , Abo_42 from t_t1 where c1 < 53.2 ;";
		SelectExtractor extractor = new SelectExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals("gh_cx", extractor.getColumnsNames().get(0));
		assertEquals("t_t1", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertEquals("c1", extractor.getConditionFirstOperand());
		assertEquals("<", extractor.getConditionOperator());
		assertEquals("53.2", extractor.getConditionSecondOperand());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test11() {
		String statement = "alter  table t add c2 date;";
		Extractor extractor = new AlterExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals(commands.ALTERADD, extractor.getCommand());
		assertEquals("c2", extractor.getColumnsNames().get(0));
		assertEquals("t", extractor.getTableName());
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionOperator());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test12() {
		String statement = "ALTER TABLE table_name13 ADD column_name4 date";
		Extractor extractor = new AlterExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals(commands.ALTERADD, extractor.getCommand());
		System.out.println(extractor.getColumnsNames());
		assertEquals("column_name4", extractor.getColumnsNames().get(0));
		assertEquals("table_name13", extractor.getTableName());
		System.out.println(extractor.getColumnsTypes());
		assertEquals("date", extractor.getColumnsTypes().get(0));
		assertNull(extractor.getDatabaseName());
		assertNull(extractor.getConditionOperator());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test13() {
		String statement = "use monster_university ;";
		Extractor extractor = new UseExtractor(statement);
		assertTrue(extractor.isValid());
		assertEquals(commands.USE, extractor.getCommand());
		assertEquals("monster_university", extractor.getDatabaseName());
		assertNull(extractor.getColumnsNames());
		assertNull(extractor.getTableName());
		assertNull(extractor.getConditionOperator());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test14() {
		String statement = "CREATE TABLE table_name1 (column_name1 varchar, column_name2 int, column_name3 date) ;";
		Extractor extractor = new CreateExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test15() {
		String statement = "insert into table_name1 values ('kk',5,'1999-18-10')";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test16() {
		String statement = "insert into t1 values (1 , 50.9 , 'omasdf' , '1996-10-04' )";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test17() {
		String statement = "INSERT INTO table_name4(column_NAME1, COLUMN_name3, column_name2) VALUES('value1', '2011-01-25', 4)";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test18() {
		String statement = "UPDATE table_name7 SET column_name1='1111111111', COLUMN_NAME2=2222222,column_name3='333333333'";
		Extractor extractor = new UpdateExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		// System.out.println("columnNames " + extractor.getColumnsNames());
		// System.out.println("columnTypes " + extractor.getColumnsTypes());
		// System.out.println("values " + extractor.getValues() + " " +
		// extractor.getClass().getSimpleName());
	}

	@Test
	public void test19() {
		String statement = "INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2,column_name4) VALUES ('value1', '2011-01-25', 3, 1.3)";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		//System.out.println("columnNames " + extractor.getColumnsNames() + "   " + extractor.getColumnsNames().size());
		//System.out.println("columnTypes " + extractor.getColumnsTypes());
		//System.out.println("values " + extractor.getValues() + " " + extractor.getClass().getSimpleName());
	}

	@Test
	public void test20() {
		String statement = "INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2,column_name4) VALUES ('value2', '2011-02-11', -123, 3.14159)";
		Extractor extractor = new InsertExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		//System.out.println("columnNames " + extractor.getColumnsNames() + "   " + extractor.getColumnsNames().size());
		//System.out.println("columnTypes " + extractor.getColumnsTypes());
		//System.out.println("values " + extractor.getValues() + " " + extractor.getClass().getSimpleName());
	}
	
	@Test
	public void test21() {
		String statement = "UPDATE table_name8 SET COLUMN_NAME2=222222, column_name3='1993-10-03'WHERE coLUmn_NAME1='value1'";
		Extractor extractor = new UpdateExtractor(statement);
		assertTrue("statement is not valid", extractor.isValid());
		//System.out.println("1st operand  " + extractor.getConditionFirstOperand());
		//System.out.println("operator  " + extractor.getConditionOperator());
		//System.out.println("2st operand  " + extractor.getConditionSecondOperand());
		//System.out.println("columnNames " + extractor.getColumnsNames() + "   " + extractor.getColumnsNames().size());
		//System.out.println("columnTypes " + extractor.getColumnsTypes());
		//System.out.println("values " + extractor.getValues() + " " + extractor.getClass().getSimpleName());
	}
	
	@Test
	public void test22() {
		String statement = "INSERT INTO table_name5(invalid_column_name1, column_name3,column_name2) VALUES ('value1', 'value3', 4)";
		Extractor extractor = new InsertExtractor(statement);
		assertFalse(extractor.isValid());
		//System.out.println("1st operand  " + extractor.getConditionFirstOperand());
		//System.out.println("operator  " + extractor.getConditionOperator());
		//System.out.println("2st operand  " + extractor.getConditionSecondOperand());
		//System.out.println("columnNames " + extractor.getColumnsNames() + "   " + extractor.getColumnsNames().size());
		//System.out.println("columnTypes " + extractor.getColumnsTypes());
		//System.out.println("values " + extractor.getValues() + " " + extractor.getClass().getSimpleName());
	}
	
}