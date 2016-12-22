package dbms;

import java.nio.file.Path;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fileManager.IFileWriter;
import fileManager.JsonWriter;
import fileManager.WriterType;
import fileManager.XMLWriter;

/**
 * Please use constructor to intialize the variables.
 *
 * @author omar
 *
 */
public class TableManager {
    /**
     * an Object of MiniTable.
     */
    private MiniTable miniTable;
    /**
     * an Object of IFileWriter.
     */
    private IFileWriter fileCreator;

    /**
     * a Constructor of the Table Manager.
     */
    public TableManager() {
        miniTable = null;
        fileCreator = null;
    }

    /**
     * @param mT
     *            a MiniTable to be set as CurrentTable
     */
    public final void setCurrentTable(final MiniTable mT) {
        this.miniTable = mT;
    }

    /**
     *
     * @param tName
     *            Table name
     * @param dbPath
     *            Path of the database.
     * @param cols
     *            List of Columns.
     * @param fileType
     *            the Type of file to be written.
     * @return MiniTable (the created table)
     * @throws SQLException
     *             if a database access error occurs
     */
    public final MiniTable createTable(final String tName,
            final Path dbPath,
            final List<Column> cols,
            final WriterType fileType) throws SQLException {
        miniTable = new MiniTable(tName, dbPath, cols);
        switch (fileType) {
        case XMLDB:
            fileCreator = new XMLWriter();
            break;
        case ALTDB:
            fileCreator = new JsonWriter();
            break;
        default:
            throw new SQLException("unsupported file type");
        }
        fileCreator.write(tName, dbPath, cols, miniTable);
        return miniTable;
    }

    /**
     * Removes the MiniTable of this TableManager.
     */
    public final void dropTable() {
        try {
            this.miniTable.getDataFile().delete();
            if (this.miniTable.getDtDFile() != null) {
                this.miniTable.getDtDFile().delete();
            }
            // System.out.println("Bye Bye " + this.miniTable.getName());
            this.miniTable = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     *
     * @param values
     *            the values of the new row
     * @throws SQLException
     *             a error in the inserted data
     */
    public final void insert(final List<String> values)
            throws SQLException {
        if (values.size() != this.miniTable.gettColumns().size()) {
            throw new SQLException(
                    "number of values entered isn't compatible with number of columns of ("
                            + this.miniTable.getName()
                            + "). \nnumber of columns required = "
                            + this.miniTable.gettColumns().size());
        }
        String[] checker = new String[values.size()];
        checker = values.toArray(checker);
        checkType(checker);
        int i = 0;
        for (Column col : this.miniTable.gettColumns()) {
            if (i < values.size()) {
                col.getColElements().add(values.get(i));
                i++;
            }
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    /**
     *
     * @param columns
     *            List of Columns
     * @param values
     *            List of Values
     * @throws SQLException
     *             error in the inserted data
     */
    public final void insert(final List<String> columns,
            final List<String> values)
            throws SQLException {
        // for (int i = 0; i < columns.size(); i++) {
        // System.out.println(i + ",," + columns.get(i) + " ");
        // }
        // System.out.println();
        // for (int i = 0; i < values.size(); i++) {
        // System.out.println(i + ",," + values.get(i) + " ");
        // }
        // System.out.println();
        if (columns.size() != values.size()) {
            throw new SQLException(
                    "number of values entered isn't compatible with number of columns of ("
                            + this.miniTable.getName()
                            + "). \nnumber of columns required = "
                            + this.miniTable.gettColumns().size());
        }
        String[] row = new String[this.miniTable.gettColumns()
                .size()];
        int i = 0;
        for (Column col : this.miniTable.gettColumns()) {
            for (String colName : columns) {
                if (colName.equalsIgnoreCase(col.getName())) {
                    row[i] = values.get(columns.indexOf(colName));
                    break;
                } else {
                    row[i] = "null";
                }
            }
            i++;
        }
        checkType(row);
        int j = 0;
        for (Column col : this.miniTable.gettColumns()) {
            if (j < row.length) {
                col.getColElements().add(row[j]);
                j++;
            }
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    /**
     *
     * @param row
     *            the inserted row
     * @throws SQLException
     *             wrong type.
     */
    private void checkType(final String[] row) throws SQLException {
        int j = 0;
        for (Column col : this.miniTable.gettColumns()) {
            if (col.getType().equalsIgnoreCase("int")
                    && row[j] != "null") {
                checkInt(row[j]);
            } else if (col.getType().equalsIgnoreCase("varchar")) {
                checkStr(row[j]);
            } else if (col.getType().equalsIgnoreCase("date")
                    && row[j] != "null") {
                checkDate(row[j]);
            } else if (col.getType().equalsIgnoreCase("float")
                    && row[j] != "null") {
                checkFloat(row[j]);
            } else if (row[j] == "null"
                    && (col.getType().equalsIgnoreCase("date")
                            || col.getType().equalsIgnoreCase("int")
                            || col.getType()
                                    .equalsIgnoreCase("float"))) {
                return;
            } else {
                throw new SQLException("Unsupported Type");
            }
            j++;
        }
    }

    /**
     *
     * @param s
     *            updated or inserted cell
     * @throws SQLException
     *             wrong type.
     */
    private void checkDate(final String s) throws SQLException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        try {
            formatter.parse(s);

        } catch (ParseException e) {
            throw new SQLException("wrong Date dataType");
        }
    }

    /**
     *
     * @param s
     *            updated or inserted cell
     * @throws SQLException
     *             wrong type.
     */
    private void checkInt(final String s) throws SQLException {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            throw new SQLException(
                    "type of data you entered is not compatible with column dataType");
        }

    }

    /**
     *
     * @param s
     *            updated or inserted cell
     * @throws SQLException
     *             wrong type.
     */
    private void checkStr(final String s) {

    }

    /**
     *
     * @param s
     *            updated or inserted cell
     * @throws SQLException
     *             wrong type.
     */
    private void checkFloat(final String s) throws SQLException {
        try {
            Float.parseFloat(s);
        } catch (Exception e) {
            throw new SQLException(
                    "type of data you entered is not compatible with column dataType");
        }

    }

    public List<Column> select(List<String> columnsName,
            List<Integer> rowNumber) throws SQLException {
        List<Column> returnCol = new ArrayList<Column>();
        if (columnsName == null) {
            selectAll(rowNumber, returnCol);
            return returnCol;
        } else if (columnsName.size() > miniTable.gettColumns()
                .size()) {
            throw new SQLException(
                    "number of columns exceed the number of columns of "
                            + this.miniTable.getName() + " table");
        }

        int flag = 0;
        for (String colName : columnsName) {
            for (Column col : this.miniTable.gettColumns()) {
                if (colName.equalsIgnoreCase(col.getName())) {
                    Column myCol = new Column();
                    for (int i = 0; i < rowNumber.size(); i++) {
                        if (rowNumber.get(i) < this.miniTable
                                .gettColumns()
                                .get(i).getColElements().size()) {
                            myCol.setName(col.getName());
                            myCol.setType(col.getType());
                            myCol.getColElements().add(
                                    col.getColElements()
                                            .get(rowNumber.get(i)));
                        } else {
                            throw new SQLException(
                                    "wrong row number");
                        }
                    }
                    returnCol.add(myCol);
                    flag++;
                    break;
                }
            }

        }
        if (flag < columnsName.size()) {
            throw new SQLException("wrong column name");
        }
        return returnCol;
    }

    private void selectAll(List<Integer> rowNumber,
            List<Column> returnCol)
            throws SQLException {
        for (Column c : this.miniTable.gettColumns()) {
            Column myCol = new Column();
            for (int i = 0; i < rowNumber.size(); i++) {
                if (rowNumber.get(i) < this.miniTable.gettColumns()
                        .get(i)
                        .getColElements().size()) {
                    myCol.setName(c.getName());
                    myCol.setType(c.getType());
                    myCol.getColElements()
                            .add(c.getColElements()
                                    .get(rowNumber.get(i)));
                } else {
                    throw new SQLException("wrong row number");
                }
            }
            returnCol.add(myCol);
        }
    }

    public List<Column> select(List<String> columnsName)
            throws SQLException {
        if (columnsName == null) {
            return this.miniTable.gettColumns();
        } else if (columnsName.size() > miniTable.gettColumns()
                .size()) {
            throw new SQLException(
                    "number of columns exceed the number of columns of "
                            + this.miniTable.getName() + " table");
        }

        int flag = 0;
        List<Column> returnCol = new ArrayList<Column>();
        for (String colName : columnsName) {
            for (Column col : this.miniTable.gettColumns()) {

                if (colName.equalsIgnoreCase(col.getName())) {
                    returnCol.add(col);
                    flag++;
                    break;
                }
            }
        }
        if (flag != columnsName.size()) {
            throw new SQLException("wrong column name");
        }
        return returnCol;
    }

    public void delete(List<Integer> rowNumber) throws SQLException {
        if (rowNumber == null) {
            for (Column c : this.miniTable.gettColumns()) {
                c.getColElements().clear();
            }
        } else {
            for (Column c : this.miniTable.gettColumns()) {
                for (int i = 0; i < rowNumber.size(); i++) {
                    int index = rowNumber.get(i) - i;
                    // System.out.println("TableMangar >>" +
                    // c.getColElements());
                    if (index < c.getColElements().size()) {
                        c.getColElements().remove(index);
                    } else {
                        throw new SQLException("wrong row number");
                    }
                }
            }
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    public void update(List<String> columnsName, List<String> Values)
            throws SQLException {
        int j = 0;
        int flag = 0;
        for (String colName : columnsName) {
            for (Column c : this.miniTable.gettColumns()) {

                if (colName.equalsIgnoreCase(c.getName())) {
                    flag++;
                    checkTypeUpdate(Values.get(j), c);
                    for (int i = 0; i < this.miniTable.gettColumns()
                            .get(0)
                            .getColElements().size(); i++) {
                        c.getColElements().set(i, Values.get(j));
                    }
                    break;
                }
            }
            j++;
        }
        if (flag != columnsName.size()) {
            throw new SQLException("wrong column name");
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    public void update(List<String> columnsName, List<String> Values,
            List<Integer> rowNumber) throws SQLException {

        int j = 0;
        int flag = 0;
        for (String colName : columnsName) {
            for (Column c : this.miniTable.gettColumns()) {
                if (colName.equalsIgnoreCase(c.getName())) {
                    flag++;
                    for (int i = 0; i < rowNumber.size(); i++) {
                        if (rowNumber.get(i) < this.miniTable
                                .gettColumns()
                                .get(i).getColElements().size()) {
                            checkTypeUpdate(Values.get(j), c);
                            c.getColElements().set(rowNumber.get(i),
                                    Values.get(j));
                        } else {
                            throw new SQLException(
                                    "wrong row number");
                        }
                    }
                }
            }
            j++;
        }
        if (flag != columnsName.size()) {
            throw new SQLException();
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    private void checkTypeUpdate(String s, Column c)
            throws SQLException {
        if (c.getType().equalsIgnoreCase("int")) {
            checkInt(s);
        } else if (c.getType().equalsIgnoreCase("varchar")) {
            checkStr(s);
        } else if (c.getType().equalsIgnoreCase("float")) {
            checkFloat(s);
        } else if (c.getType().equalsIgnoreCase("date")) {
            checkDate(s);
        }
    }

    /**
     *
     * @param columnsName
     *            The Columns names given by user
     * @return those Columns with distinct (different) values only. a column may
     *         contain many duplicate values. we Remove them to list the
     *         different values
     * @throws SQLException
     */
    public List<Column> selectDistinct(List<String> columnsName)
            throws SQLException {
        List<Column> returnCol = select(columnsName);
        List<Column> copy = new ArrayList<Column>(returnCol.size());
        for (Column col : returnCol) {
            copy.add(col.deepCopy());
        }
        if (containNoDup(returnCol)) {
            return returnCol;
        } else {
            List<Integer> locDup = findDisLoc(copy);
            List<Column> DisColumns = removeDup(copy, locDup);
            return DisColumns;
        }
    }

    public List<Column> selectDistinct(List<String> columnsName,
            List<Integer> rowNumber) throws SQLException {
        List<Column> returnCol = select(columnsName, rowNumber);
        List<Column> copy = new ArrayList<Column>(returnCol.size());
        for (Column col : returnCol) {
            copy.add(col.deepCopy());
        }
        if (containNoDup(returnCol)) {
            return returnCol;
        } else {
            List<Integer> locDup = findDisLoc(copy);
            List<Column> DisColumns = removeDup(copy, locDup);
            return DisColumns;
        }
    }

    /**
     *
     * @param cols
     *            the Selected columns
     * @return a Boolean for Whether a selected column contains no duplicate
     *         Elements.
     */
    private boolean containNoDup(List<Column> cols) {
        List<String> list;
        Set<String> set;
        for (Column col : cols) {
            list = col.getColElements();
            set = new HashSet<String>(list);
            if (set.size() == list.size()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param Cols
     *            the Selected Columns
     * @return the Distinct Elements Location without Duplicates
     */
    private List<Integer> findDisLoc(List<Column> Cols) {
        List<String> nonDupList;
        List<Integer> temp = new ArrayList<Integer>();
        List<Integer> actualDupLoc = new ArrayList<Integer>();
        for (Column col : Cols) {
            int i = 0;
            nonDupList = new ArrayList<String>();
            // Adding Distinct Elements Location (Duplicate Accepted)
            for (String dupWord : col.getColElements()) {
                if (!nonDupList.contains(dupWord)) {
                    nonDupList.add(dupWord);
                    temp.add(i);
                }
                i++;
            }
        }
        // removing Duplicate Locations.
        actualDupLoc = actualLoc(temp);
        return actualDupLoc;
    }

    /**
     *
     * @param temp
     *            the Distinct Elements Location with no matter if there are
     *            Duplicates or not
     * @return the Distinct Elements Location without Duplicates
     */
    private List<Integer> actualLoc(List<Integer> temp) {
        List<Integer> Dup = new ArrayList<Integer>();
        for (int k = 0; k < temp.size(); k++) {
            // Adding rows number without duplicate.
            if (!Dup.contains(temp.get(k))) {
                Dup.add(temp.get(k));
            }
        }
        return Dup;
    }

    /**
     *
     * @param cols
     *            The Selected Columns.
     * @param minLoc
     *            The Selected Columns without Duplicates
     * @return
     */
    private List<Column> removeDup(List<Column> cols,
            List<Integer> minLoc) {
        // removing duplicate rows.
        for (int i = 0; i < cols.get(0).getColElements()
                .size(); i++) {
            if (!minLoc.contains(i)) {
                for (Column col : cols) {
                    col.getColElements().remove(i);
                    // ColElements' Size decreases.
                    for (int j = 0; j < minLoc.size(); j++) {
                        if (minLoc.get(j) > i) {
                            minLoc.set(j, minLoc.get(j) - 1);
                        }
                    }
                    i--;
                }
            }
        }
        return cols;
    }

    /**
     * Adding a column in a table
     *
     * @param cols
     *            a List of new Columns to be added to the table
     * @throws SQLException
     */
    public void alterAdd(List<Column> cols) throws SQLException {
        for (Column col : cols) {
            for (int i = 0; i < this.miniTable.gettColumns().get(0)
                    .getColElements().size(); i++) {
                col.getColElements().add("null");
            }
            this.miniTable.gettColumns().add(col);
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

    /**
     * Removing a column in a table
     *
     * @throws SQLException
     */
    public void alterDelete(List<String> columnsName)
            throws SQLException {
        int flag = 0;
        for (String colName : columnsName) {
            for (Column col : this.miniTable.gettColumns()) {

                if (colName.equalsIgnoreCase(col.getName())) {
                    this.miniTable.gettColumns().remove(col);
                    flag++;
                    break;
                }
            }
        }
        if (flag != columnsName.size()) {
            throw new SQLException("wrong column name");
        }
        fileCreator.write(this.miniTable.getName(),
                this.miniTable.getDBPath(),
                this.miniTable.gettColumns(), this.miniTable);
    }

}