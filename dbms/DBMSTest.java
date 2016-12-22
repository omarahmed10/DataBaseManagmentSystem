package dbms;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dbms.Column;
import dbms.DataBaseMS;
import fileManager.WriterType;

public class DBMSTest {

    String tmp = System.getProperty("java.io.tmpdir");
    Path p = Paths.get(
            tmp + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
    DataBaseMS DBMS = new DataBaseMS();
    public ArrayList<Column> initColumns() {
        ArrayList<Column> ali = new ArrayList<Column>();
        ArrayList<String> col1 = new ArrayList<String>();
        col1.add("1");
        col1.add("2");
        col1.add("3");
        col1.add("4");
        ArrayList<String> col2 = new ArrayList<String>();
        col2.add("Student1");
        col2.add("Student2");
        col2.add("Student3");
        col2.add("Student4");
        ArrayList<String> col3 = new ArrayList<String>();
        col3.add("2005-11-6");
        col3.add("2005-11-6");
        col3.add("1995-2-21");
        col3.add("1999-05-30");
        Column ali1 = new Column();
        ali1.setColElements(col1);
        ali1.setName("Id");
        ali1.setType("int");
        Column ali2 = new Column();
        ali2.setColElements(col2);
        ali2.setName("Name");
        ali2.setType("varchar");
        Column ali3 = new Column();
        ali3.setColElements(col3);
        ali3.setName("Date");
        ali3.setType("date");
        ali.add(ali1);
        ali.add(ali2);
        ali.add(ali3);
        return ali;
    }

    @Test(expected = SQLException.class)
    public void dropNonExistenceDB() throws SQLException {
        DBMS.dropDB("Students");
    }

    @Test(expected = SQLException.class)
    public void useNonExistenceDB() throws SQLException {
        DBMS.useDB("Students");
    }

    /**
     * Testing insert Elements in new row.
     */
    @Test
    public void testInsertFullRow() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        ArrayList<String> values = new ArrayList<String>();
        values.add("5");
        values.add("Student5");
        values.add("2001-03-11");
        DBMS.insertIntoTable("sTable", values);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        assertEquals(5, test.get(0).getColElements().size());
        // Cells in the inserted row
        assertEquals("5",
                test.get(0).getColElements().get(4));
        assertEquals("Student5",
                test.get(1).getColElements().get(4));
        assertEquals("2001-03-11",
                test.get(2).getColElements().get(4));

    }

    /**
     * Testing insert Elements in just Specific cells (rest of the cell must be
     * assigned to "null")
     */
    @Test
    public void testInsertSpeCells() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        ArrayList<String> values2 = new ArrayList<String>();
        values2.add("Student5");
        ArrayList<String> columns2 = new ArrayList<String>();
        columns2.add("Name");
        DBMS.insertIntoTable("sTable", columns2, values2);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        assertEquals(5, test.get(0).getColElements().size());
        // Cells in the inserted row
        assertEquals("null",
                test.get(0).getColElements().get(4));
        assertEquals("Student5",
                test.get(1).getColElements().get(4));
        assertEquals("null",
                test.get(2).getColElements().get(4));

    }

    /**
     * Testing insert Element with Wrong Type
     */
    @Test(expected = SQLException.class)
    public void testInsertType() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        ArrayList<String> values = new ArrayList<String>();
        values.add("5");
        values.add("Student5");
        values.add("a5");
        DBMS.insertIntoTable("sTable", values);
    }

    /**
     * Testing Select specific element from table implement.
     */
    @Test
    public void testSelectSpeCell() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("Date");
        ArrayList<Integer> rowNumber = new ArrayList<Integer>();
        rowNumber.add(3);
        List<Column> s = DBMS.selectFromTable("sTable", cols, rowNumber);
        assertEquals(1, s.size());
        assertEquals("Date", s.get(0).getName());
        assertEquals("date", s.get(0).getType());
        assertEquals("1999-05-30", s.get(0).getColElements().get(0));

    }

    /**
     * Testing select a specific row form the table.
     */
    @Test
    public void testSelectRow() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        List<Integer> rowNumber2 = new ArrayList<Integer>();
        rowNumber2.add(1);
        List<Column> sec = DBMS.selectFromTable("sTable", null, rowNumber2);
        assertEquals(3, sec.size());
        // Cell 1 in 2nd row
        assertEquals("Id", sec.get(0).getName());
        assertEquals("int", sec.get(0).getType());
        assertEquals("2", sec.get(0).getColElements().get(0));
        // Cell 2 in 2nd row
        assertEquals("Name", sec.get(1).getName());
        assertEquals("varchar", sec.get(1).getType());
        assertEquals("Student2", sec.get(1).getColElements().get(0));
        // Cell 3 in 2nd row
        assertEquals("Date", sec.get(2).getName());
        assertEquals("date", sec.get(2).getType());
        assertEquals("2005-11-6", sec.get(2).getColElements().get(0));

    }

    @Test
    /**
     * Testing deleting specific row from the table.
     */
    public void testDelete() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        List<Integer> rowNumber3 = new ArrayList<Integer>();
        rowNumber3.add(0);
        DBMS.deleteFromTable("sTable", rowNumber3);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        // Number of rows
        assertEquals(3, test.get(0).getColElements().size());
        // testing Cells in the new row 0
        assertEquals("2",
                test.get(0).getColElements().get(0));
        assertEquals("Student2",
                test.get(1).getColElements().get(0));
        assertEquals("2005-11-6",
                test.get(2).getColElements().get(0));
    }

    @Test
    /**
     * Testing updating specific row
     */
    public void testUpdate() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        ArrayList<String> values3 = new ArrayList<String>();
        values3.add("10");
        values3.add("Student10");
        values3.add("1988-2-10");
        ArrayList<String> columns3 = new ArrayList<String>();
        columns3.add("Id");
        columns3.add("Name");
        columns3.add("Date");
        ArrayList<Integer> rowNumber3 = new ArrayList<Integer>();
        rowNumber3.add(0);
        DBMS.updateTable("sTable", columns3, values3, rowNumber3);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        assertEquals(4, test.get(0).getColElements().size());
        assertEquals("10",
                test.get(0).getColElements().get(0));
        assertEquals("Student10",
                test.get(1).getColElements().get(0));
        assertEquals("1988-2-10",
                test.get(2).getColElements().get(0));
    }

    @Test
    /**
     * Testing Adding a Column.
     */
    public void testAlterAdd() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        List<Column> cols = new ArrayList<Column>();
        Column a = new Column();
        a.setName("Number");
        a.setType("Float");
        cols.add(a);
        DBMS.alterAdd("sTable", cols);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        assertEquals(4, test.get(3).getColElements().size());
        assertEquals("null",
                test.get(3).getColElements().get(0));
        assertEquals("null",
                test.get(3).getColElements().get(1));
        assertEquals("null",
                test.get(3).getColElements().get(2));
        assertEquals("null",
                test.get(3).getColElements().get(3));
    }

    @Test
    /**
     * Testing Deleting a Column.
     */
    public void testAlterDelete() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        List<String> columnsName = new ArrayList<String>();
        columnsName.add("Name");
        DBMS.alterDelete("sTable", columnsName);
        List<Column> test = DBMS.selectFromTable("sTable", null);
        assertEquals(2, test.size());
        assertEquals("Id",
                test.get(0).getName());
        assertEquals("Date",
                test.get(1).getName());
    }

    @Test
    /**
     * Testing Selecting Distinct.
     */
    public void testSelectDistinct() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.createTable("sTable", p, initColumns(), WriterType.ALTDB);
        List<String> columnsName = new ArrayList<String>();
        columnsName.add("Date");
        List<Column> test = DBMS.selectDistinctFromTable("sTable", columnsName);
        assertEquals(3, test.get(0).getColElements().size());
        assertEquals("2005-11-6",
                test.get(0).getColElements().get(0));
        assertEquals("1995-2-21",
                test.get(0).getColElements().get(1));
        assertEquals("1999-05-30",
                test.get(0).getColElements().get(2));

    }

    @Test(expected = SQLException.class)
    public void testDropDB() throws SQLException {
        DBMS.createDB(p, "Students");
        DBMS.useDB("Students");
        DBMS.dropDB("Students");
        DBMS.useDB("Students");
    }
}