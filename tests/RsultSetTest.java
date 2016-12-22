package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jdbc.ResultSet;
import jdbc.Statement;

public class RsultSetTest {
    List<String> cNames = new ArrayList<>();
	List<String> cTypes = new ArrayList<>();
	List<List<String>> rowValues = new ArrayList<>();
	List<String> row = new ArrayList<>();
	public ResultSet initialize(){
		cNames.add("ID");
		cNames.add("First Name");
		cNames.add("Last Name");
		cNames.add("Age");
		cTypes.add("int");
		cTypes.add("varchar");
		cTypes.add("varchar");
		cTypes.add("float");
		row.add("1");
		row.add("mostafa");
		row.add("Ahmed");
		row.add("20");
		rowValues.add(row);
		List<String> row2 = new ArrayList<>();
		row2.add("2");
		row2.add("Ali");
		row2.add("saeed");
		row2.add("21.5");
		rowValues.add(row2);
		Statement s = null;
		ResultSet rs = new ResultSet(cNames, cTypes, rowValues, "TName",s);
		return rs;
	}
	
	@Test
	public void testAbsolute()throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true,rs.absolute(1));
	}
	@Test
	public void testNext() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true,rs.next());
		assertEquals(true,rs.next());
		assertEquals(false,rs.next());
		assertEquals(false,rs.next());
	}
	@Test
	public void testFindColumn() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(1, rs.findColumn("ID"));
		assertEquals(2, rs.findColumn("First Name"));
		assertEquals(3, rs.findColumn("Last Name"));
		assertEquals(4, rs.findColumn("Age"));
	}
	@Test
	public void testgetInt() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		rs.absolute(1);
		assertEquals(1, rs.getInt(1));
		assertEquals(1,rs.getInt("ID"));
		rs.next();
		assertEquals(2,rs.getInt("ID"));
	}
	@Test
	public void testgetString() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		rs.absolute(1);
		assertEquals("mostafa", rs.getString(2));
		assertEquals("Ahmed",rs.getString(3));
		rs.next();
		assertEquals("Ali",rs.getString(2));
		assertEquals("saeed",rs.getString(3));
	}
	@Test
	public void testgetFloat() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(1));
		assertEquals(20, rs.getFloat(4), .01);
		assertEquals(20, rs.getFloat("Age"), .01);
		assertEquals(true, rs.next());
		assertEquals(21.5, rs.getFloat(4), .01);
	}
	@Test
	public void testprevious() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(2));
		assertEquals(true, rs.previous());
		assertEquals(false, rs.previous());
	}
	@Test
	public void testAfterLast() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(1));
		assertEquals(false, rs.isAfterLast());
		rs.afterLast();
		assertEquals(true, rs.isAfterLast());
	}
	@Test
	public void testBeforeFirst() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(1));
		assertEquals(false, rs.isBeforeFirst());
		rs.beforeFirst();
		assertEquals(true, rs.isBeforeFirst());
	}
	@Test
	public void testIsFirst() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(2));
		assertEquals(false, rs.isFirst());
		rs.first();
		assertEquals(true, rs.isFirst());
	}
	@Test
	public void testIsLast() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(1));
		assertEquals(false, rs.isLast());
		rs.last();
		assertEquals(true, rs.isLast());
	}
	@Test
	public void testClose() throws SQLException{
		RsultSetTest test = new RsultSetTest();
		ResultSet rs = test.initialize();
		assertEquals(true, rs.absolute(1));
		assertEquals(false, rs.isClosed());
		rs.close();
		assertEquals(true, rs.isClosed());
	}
}