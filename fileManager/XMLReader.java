package fileManager;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dbms.Column;
import dbms.MiniTable;

public class XMLReader {
	private DocumentBuilder builder;
	private NodeList nodeList;

	public XMLReader() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MiniTable loadTable(String tName, Path dbPath) {
		File file = new File("DataBases" + File.separator + dbPath,
				tName + ".xml");

		if (!file.exists()) {
			return null;
		}

		Document document = null;
		try {
			document = builder.parse(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		nodeList = document.getDocumentElement().getChildNodes();
		MiniTable lTable = new MiniTable(
				document.getDocumentElement().getAttribute("name"), dbPath);
		loadInfo(nodeList, lTable);
		return lTable;
	}

	private void loadInfo(NodeList nodeList, MiniTable table) {
		List<Column> loadList = new ArrayList<Column>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Column column = new Column();
				column.setName(node.getAttributes().getNamedItem("name")
						.getNodeValue());
				column.setType(node.getAttributes().getNamedItem("type")
						.getNodeValue());
				NodeList childNodes = node.getChildNodes();

				for (int j = 0; j < childNodes.getLength(); j++) {
					Node cNode = childNodes.item(j);
					if (cNode instanceof Element) {
						String content = cNode.getTextContent();
						column.getColElements().add(content);
					}
				}
				loadList.add(column);
			}
		}
		if (loadList.size() == 0) {
			table.settColumns(null);
		} else {
			table.settColumns(loadList);
		}
	}
}
