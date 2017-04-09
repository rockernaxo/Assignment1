import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import CIM.*;

public class ReadCIMXML {

	public static void main(String[] args) {

		String[] tags = { "cim:Breaker", "cim:BaseVoltage", "cim:VoltageLevel", "cim:Substation",
				"cim:SynchronousMachine" };

		ArrayList<NodeList> container = new ArrayList<NodeList>();
		ArrayList<NodeList> containerSSH = new ArrayList<NodeList>();

		// At the moment, the objects will be stored in arrayList, but later
		// they should be sent to the database.
		ArrayList<CircuitBreaker> breakerList = new ArrayList<CircuitBreaker>();
		ArrayList<BaseVoltage> bVoltList = new ArrayList<BaseVoltage>();
		ArrayList<Substation> subList = new ArrayList<Substation>();
		ArrayList<VoltageLevel> voltLvlList = new ArrayList<VoltageLevel>();
		ArrayList<SynchronousMachine> synMach = new ArrayList<SynchronousMachine>();

		try {

			// Importing the XML EQ and SSH files
			File SSHFile = new File("C:\\Users\\naxop\\Desktop\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml");
			File EQFile = new File("C:\\Users\\naxop\\Desktop\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml");

			// Create and initiate the XML parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document docEQ = dBuilder.parse(EQFile);
			Document docSSH = dBuilder.parse(SSHFile);

			// Normalize the CIM XML File
			docEQ.getDocumentElement().normalize();
			docSSH.getDocumentElement().normalize();

			// Add all NodeLists from the XML file into an array
			containerSSH.add(docSSH.getElementsByTagName("cim:Breaker"));
			containerSSH.add(docSSH.getElementsByTagName("cim:SynchronousMachine"));
			for (int i = 0; i < tags.length; i++) {
				container.add(docEQ.getElementsByTagName(tags[i]));
			}

			// System.out.println(container.get(0).item(0).getNodeName());
			// System.out.println(container.get(1).item(0).getNodeName());

			/*
			 * // Extract all the base voltage for (int i = 0; i <
			 * volList.getLength(); i++) { baseVList.add(new
			 * BaseVoltage(extractAtribute(volList.item(i), "rdf:ID"),
			 * Double.parseDouble(extractTag(volList.item(i),
			 * "cim:BaseVoltage.nominalVoltage")))); }
			 * 
			 * // Extract all the circuit breakers for (int i = 0; i <
			 * cbList.getLength(); i++) { breakerList.add(new
			 * CircuitBreaker(true, // Here it should be // the state opened //
			 * from the other // file extractAtribute(cbList.item(i), "rdf:ID"),
			 * extractTag(cbList.item(i), "cim:IdentifiedObject.name"),
			 * extractTag(cbList.item(i),
			 * "cim:IdentifiedObject.EquipmentContainer"))); }
			 */
			// Generalization of the previous method

			for (int i = 0; i < container.size(); i++) {

				for (int j = 0; j < container.get(i).getLength(); j++) {
					// Add a new object to the ArrayList
					String selec = container.get(i).item(j).getNodeName();
					switch (selec) {
					case "cim:Breaker":
						breakerList.add(new CircuitBreaker(container.get(i).item(j), containerSSH.get(0).item(j)));
						break;
					case "cim:BaseVoltage":
						bVoltList.add(new BaseVoltage(container.get(i).item(j)));
						break;
					case "cim:Substation":
						subList.add(new Substation(container.get(i).item(j)));
						break;
					case "cim:VoltageLevel":
						voltLvlList.add(new VoltageLevel(container.get(i).item(j)));
						break;
					case "cim:SynchronousMachine":
						synMach.add(new SynchronousMachine(container.get(i).item(j), containerSSH.get(1).item(j)));
						break;
					}
				}
			}
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Now it's embedded onto the CIM class public static String extractTag(Node
	 * node, String tag) { // Method to extract a tag from a node
	 * //ArrayList<String> output = new ArrayList<String>(); Element element =
	 * (Element) node;
	 * 
	 * return element.getElementsByTagName(tag).item(0).getTextContent(); /*
	 * This could be useful to automate the process for (String item : tag) {
	 * System.out.println(item);
	 * element.getElementsByTagName("name").item(0).getTextContent();
	 * output.add(element.getElementsByTagName("name").item(0).getTextContent())
	 * ;
	 * output.add(element.getElementsByTagName(item).item(0).getTextContent());
	 * }
	 * 
	 * return output;
	 * 
	 * 
	 * }
	 * 
	 * public static String extractAttribute(Node node, String attribute) { //
	 * Method to extract an attribute from a node Element element = (Element)
	 * node; return element.getAttribute(attribute); }
	 * 
	 * public static String extractAttFromTag(Node node, String tag, String
	 * attribute) { // Method to extract an attribute from a node's tag Element
	 * element = (Element) node; Element subElement = (Element)
	 * element.getElementsByTagName(tag).item(0); return
	 * subElement.getAttribute(attribute); }
	 */
}
