package fileManager;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.List;

import dbms.Column;

public class DTD {
	
	private File dtd;
	public void creat(List<Column> cols,String name){
		dtd = new File(name);
		ArrayList<String> lines = new ArrayList<>();
		lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		String s ="(";
		for (int i = 1; i <= cols.size(); i++) {
			s+="column"+i;
			if(i<cols.size()){
				s+=",";
			}
		}
		s+=")";
		String fLine = "<!ELEMENT table "+s+">";
		String sLine = "<!ATTLIST table name CDATA #REQUIRED>";
		lines.add(fLine);
		lines.add(sLine);
		for (int i = 0; i < cols.size(); i++) {
			String line = "<!ELEMENT column"+(i+1)+" ("+cols.get(i).getName()+"+)>";
			lines.add(line);
			String att = "<!ATTLIST column"+(i+1)+" name CDATA #REQUIRED type CDATA #REQUIRED>";
			lines.add(att);
			String node = "<!ELEMENT "+cols.get(i).getName()+" (#PCDATA)>";
			lines.add(node);
		}
		try {
			Files.write(this.dtd.toPath(), lines, Charset.forName("UTF-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public File getMyDtdFile(){
		return dtd;
	}
}
