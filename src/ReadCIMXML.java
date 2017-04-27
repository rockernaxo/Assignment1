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
				"cim:SynchronousMachine", "cim:GeneratingUnit"};

		ArrayList<NodeList> container = new ArrayList<NodeList>();
		ArrayList<NodeList> containerSSH = new ArrayList<NodeList>();

		// At the moment, the objects will be stored in arrayList, but later
		// they should be sent to the database.
		ArrayList<CircuitBreaker> breakerList = new ArrayList<CircuitBreaker>();
		ArrayList<BaseVoltage> bVoltList = new ArrayList<BaseVoltage>();
		ArrayList<Substation> subList = new ArrayList<Substation>();
		ArrayList<VoltageLevel> voltLvlList = new ArrayList<VoltageLevel>();
		ArrayList<SynchronousMachine> synMach = new ArrayList<SynchronousMachine>();
		ArrayList<GeneratingUnit> genUnit = new ArrayList<GeneratingUnit>();

		try {

			// Importing the XML EQ and SSH files
			File SSHFile = new File("MicroGridTestConfiguration_T1_BE_SSH_V2.xml");
			File EQFile = new File("MicroGridTestConfiguration_T1_BE_EQ_V2.xml");

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
					case "cim:GeneratingUnit":
						genUnit.add(new GeneratingUnit(container.get(i).item(j)));
						break;
					}
				}
			}
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
