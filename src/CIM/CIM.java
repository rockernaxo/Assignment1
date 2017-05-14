package CIM;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CIM {

	protected String name, rdfID;

	public CIM(Node node) {
		this.rdfID = extractAttribute(node, "rdf:ID");
		this.name = extractTag(node, "cim:IdentifiedObject.name");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRdfID() {
		return rdfID;
	}

	public void setRdfID(String rdfID) {
		this.rdfID = rdfID;
	}

	public static String extractTag(Node node, String tag) {
		// Method to extract a tag from a node
		Element element = (Element) node;
		return element.getElementsByTagName(tag).item(0).getTextContent();
	}

	public static String extractAttribute(Node node, String attribute) {
		// Method to extract an attribute from a node
		Element element = (Element) node;
		return element.getAttribute(attribute);
	}

	public static String extractAttFromTag(Node node, String tag, String attribute) {
		// Method to extract an attribute from a node's tag
		Element element = (Element) node;
		Element subElement = (Element) element.getElementsByTagName(tag).item(0);
		return subElement.getAttribute(attribute);
	}

	public Node extractFromId(NodeList listSSH) {
		// This method is used to extract the node from the SSH file which has
		// the same rdfID as the one in the EQ file
		Node node = null;
		for (int i = 0; i < listSSH.getLength(); i++) {
			String rdfId = extractAttribute((Element) listSSH.item(i), "rdf:about").substring(1);
			if (rdfId.equals(this.rdfID)) {
				node = listSSH.item(i);
			}
		}
		return node;
	}

}
