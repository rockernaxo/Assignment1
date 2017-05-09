import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import CIM.*;

public class ReadCIMXML {

	public static void main(String[] args) {

		String[] tags = { "cim:Breaker", "cim:BaseVoltage", "cim:VoltageLevel", "cim:Substation",
				"cim:SynchronousMachine", "cim:GeneratingUnit", "cim:RegulatingControl", "cim:PowerTransformer",
				"cim:PowerTransformerEnd", "cim:EnergyConsumer", "cim:RatioTapChanger", "cim:ACLineSegment",
				"cim:Terminal", "cim:BusbarSection", "cim:ConnectivityNode" };

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
		ArrayList<RegulatingControl> regControl = new ArrayList<RegulatingControl>();
		ArrayList<PowerTransformer> powtrafo = new ArrayList<PowerTransformer>();
		ArrayList<PowerTransformerEnd> powtrafoEnd = new ArrayList<PowerTransformerEnd>();
		ArrayList<EnergyConsumer> energCons = new ArrayList<EnergyConsumer>();
		ArrayList<RatioTapChanger> ratiotap = new ArrayList<RatioTapChanger>();
		ArrayList<Lines> lines = new ArrayList<Lines>();
		ArrayList<Terminal> terminal = new ArrayList<Terminal>();
		ArrayList<BusbarSection> busbarSection = new ArrayList<BusbarSection>();
		ArrayList<ConnectivityNode> connectivityNode = new ArrayList<ConnectivityNode>();

		try {

			// Importing the XML EQ and SSH files
			//File SSHFile = new File("MicroGridTestConfiguration_T1_BE_SSH_V2.xml");
			 //File EQFile = new File("MicroGridTestConfiguration_T1_BE_EQ_V2.xml");

			// Archivo pequeño
			File SSHFile = new File("Assignment_SSH_reduced.xml");
			File EQFile = new File("Assignment_EQ_reduced.xml");

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
			containerSSH.add(docSSH.getElementsByTagName("cim:RegulatingControl"));
			containerSSH.add(docSSH.getElementsByTagName("cim:EnergyConsumer"));
			containerSSH.add(docSSH.getElementsByTagName("cim:RatioTapChanger"));
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
					case "cim:RegulatingControl":
						regControl.add(new RegulatingControl(container.get(i).item(j), containerSSH.get(2).item(j)));
						break;
					case "cim:PowerTransformer":
						powtrafo.add(new PowerTransformer(container.get(i).item(j)));
						break;
					case "cim:PowerTransformerEnd":
						powtrafoEnd.add(new PowerTransformerEnd(container.get(i).item(j)));
						break;
					case "cim:EnergyConsumer":
						energCons.add(new EnergyConsumer(container.get(i).item(j), containerSSH.get(3).item(j)));
						break;
					case "cim:RatioTapChanger":
						ratiotap.add(new RatioTapChanger(container.get(i).item(j), containerSSH.get(4).item(j)));
						break;
					case "cim:ACLineSegment":
						lines.add(new Lines(container.get(i).item(j)));
						break;
					case "cim:Terminal":
						terminal.add(new Terminal(container.get(i).item(j)));
						break;
					case "cim:BusbarSection":
						busbarSection.add(new BusbarSection(container.get(i).item(j)));
						break;
					case "cim:ConnectivityNode":
						connectivityNode.add(new ConnectivityNode(container.get(i).item(j)));
						break;
					}
				}
			}

			// Call to the topology processor
			TopologyProcessor tp= new TopologyProcessor(connectivityNode, terminal, breakerList);
			ArrayList<SuperConnectivityNode> sCNList =tp.getsCNList();
			
			// Y matrix calculation
			YMatrix Y = new YMatrix(100, sCNList, lines, terminal, bVoltList, powtrafo, powtrafoEnd);
			ComplexNumber[][] solution=Y.getY();
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
