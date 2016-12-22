package fileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dbms.Column;
import dbms.MiniTable;

public class JsonWriter implements IFileWriter {

	private File file;
	private FileWriter fWriter;
	private Gson gson;
	private String prettyJsonString;

	public JsonWriter() {
		fWriter = null;
		file = null;
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	public void write(String tName, Path dbPath, List<Column> cols,
			MiniTable requiredTable) throws SQLException {
		/* creating human readable string */
		prettyJsonString = gson.toJson(requiredTable);
		try {
			file = new File(dbPath + File.separator + tName + ".json");
//			System.out.println(file.getPath());
			fWriter = new FileWriter(file);
			fWriter.write(prettyJsonString);
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
			throw new SQLException("cannot create the file");
		}
		requiredTable.setDataFile(file);
	}

	public static void main(String[] args) {
		// GsonBuilder gson = new GsonBuilder();
		// gson.registerTypeAdapter(DataBase.class, new MyTypeAdapter());
		// gson.registerTypeAdapter(Table.class, new MySerializer());
		// gson.registerTypeAdapter(Table.class, new MyDeserializer());
		// gson.registerTypeAdapter(Table.class, new MyInstanceCreator());

//		List<Column> cols = new ArrayList<Column>();
//		Column o = new Column();
//		o.setName("kaka");
//		o.setType("varchar);
//		ArrayList<String> elements = new ArrayList<String>();
//		elements.add("keke");
//		o.setColElements(elements);
//		Column oo = new Column();
//		oo.setName("kaka");
//		oo.setType("varchar");
//		ArrayList<String> elementso = new ArrayList<String>();
//		elementso.add("keke");
//		oo.setColElements(elementso);
//		cols.add(o);
//		cols.add(oo);
//		JsonWriter jw = new JsonWriter();
		// MiniTable mT = new MiniTable("omara", "ahmedf", cols);
		// jw.write("asdf", "GSDF", cols, mT);
		// t.getDataFile().delete();
		// jw.delete();
		// Tableo t = gson.fromJson(prettyJsonString, Tableo.class);
		// System.out.println(t.name);
	}
}
