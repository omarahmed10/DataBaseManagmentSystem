package fileManager;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import dbms.Column;
import dbms.MiniTable;

public class XMLWriter implements IFileWriter {

	private DocumentBuilder documentBuilder = null;
	private Transformer transformer = null;
	private File file;

	public XMLWriter() {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();

		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		file = null;
	}

	@Override
	public void write(String tName, Path dbPath, List<Column> cols,
			MiniTable requiredTable) throws SQLException {
		file = createXml(tName, dbPath, cols);
		DTD dtdFile = new DTD();
		dtdFile.creat(cols, dbPath + File.separator + tName + ".dtd");
		requiredTable.setDataFile(file);
		requiredTable.setDtDFile(dtdFile.getMyDtdFile());
	}

	private File createXml(String tName, Path dbPath, List<Column> cols)
			throws SQLException {
		File f = null;
		Document doc = documentBuilder.newDocument();
		Element rootElement = doc.createElement("table");
		doc.appendChild(rootElement);
		Attr nameAttribute = doc.createAttribute("name");
		nameAttribute.setValue(tName);
		rootElement.setAttributeNode(nameAttribute);
		createColumns(cols, rootElement, doc);
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				tName + ".dtd");
		DOMSource source = new DOMSource(doc);
		try {
			f = new File(dbPath + File.separator + tName + ".xml");
//			System.out.println(f.getPath());
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new SQLException("cannot create the file");
		}
		return f;
	}

	private void createColumns(List<Column> cols, Element root, Document doc) {
		int counter = 1;
		for (Column column : cols) {
			Element element = doc.createElement("column" + (counter++));
			root.appendChild(element);
			Attr attribute = doc.createAttribute("name");
			attribute.setValue(column.getName());
			element.setAttributeNode(attribute);
			attribute = doc.createAttribute("type");
			attribute.setValue(column.getType());
			element.setAttributeNode(attribute);

			for (String cell : column.getColElements()) {
				Element info = doc.createElement(column.getName());
				info.appendChild(doc.createTextNode(cell));
				element.appendChild(info);
			}

		}
	}

}
