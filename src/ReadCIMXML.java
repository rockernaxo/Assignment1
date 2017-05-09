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


			ArrayList<ArrayList<Terminal>> connectNode = new ArrayList<ArrayList<Terminal>>();

			for (int i = 0; i < connectivityNode.size(); i++) {

				connectNode.add(new ArrayList<Terminal>());

				for (int j = 0; j < terminal.size(); j++) {
					// Put the terminals that have that id in a list
					if (terminal.get(j).getConnectNode().equals(connectivityNode.get(i).getRdfID())) {
						connectNode.get(i).add(terminal.get(j));
					}
				}
			}

			ArrayList<SuperConnectivityNode> sCNList = new ArrayList<SuperConnectivityNode>();
			// List of the idCN of CN with breakers
			ArrayList<Terminal> withBreakers = new ArrayList<Terminal>();

			// Now we iterate over the breakers to create the
			// SuperConnectivityNodes
			for (int i = 0; i < breakerList.size(); i++) {
				// Check if is the first or the second terminal of the breaker
				boolean firstTerminal = false;
				// Necesitamos la ID del CN? Esa variable es para preservarla
				int firstCN = -1;
				// We create a list in which we will include all the terminals
				// aggregated into the SCN
				ArrayList<Terminal> terminalList = new ArrayList<Terminal>();

				boolean isBreaker = false;

				// Look for the terminals in the connectNode list
				for (int j = 0; j < connectNode.size(); j++) {
					// Look in the list of terminals for the breaker's
					for (int k = 0; k < connectNode.get(j).size(); k++) {
						// We find the first terminal of the breaker
						if (connectNode.get(j).get(k).getCondEquip().equals(breakerList.get(i).getRdfID())) {
							// Activate the variable for the non-breaker list
							isBreaker = true;
							// Check if we found only the first terminal
							if (!firstTerminal) {
								// We have found the first terminal
								terminalList.addAll(connectNode.get(j));
								firstTerminal = true;
								firstCN = j;
							} else {
								// The second is found and the SCN can be
								// created
								terminalList.addAll(connectNode.get(j));
								sCNList.add(new SuperConnectivityNode(connectNode.get(firstCN).get(0).getConnectNode(),
										connectNode.get(j).get(k).getConnectNode(), terminalList));
							}
						}
					}

					// If there is no breaker, this is directly a SCN
					if (isBreaker) {
						isBreaker = false;
						withBreakers.add(connectNode.get(j).get(0));
					}
				}
			}

			// Check duplicity of terminals in the SCN
			for (int i = 0; i < sCNList.size(); i++) {
				// Get one SCN to compare it with others
				for (int k = 0; k < sCNList.get(i).getIdCN().size(); k++) {
					// Get the idCN from the SCN one by one
					String idCN = sCNList.get(i).getIdCN().get(k);
					for (int j = i + 1; j < sCNList.size(); j++) {
						if (sCNList.get(j).getIdCN().contains(idCN)) {
							// The SCN have a CN in common, therefore are
							// electrically equal
							// Add the idCN and merge the terminal lists to the
							// original SCN
							sCNList.get(i).addId(sCNList.get(j).getIdCN());
							sCNList.get(i).mergeTerminalLists(sCNList.get(j).getTerminalList());
							sCNList.remove(j);
						}
					}
				}
			}

			// Remove all CN with breakers from the array
			int index = 0;
			while (index < connectNode.size()) {
				int j = 0;
				boolean isBreaker = false;
				while (j < withBreakers.size() && !isBreaker) {
					if (connectNode.get(index).contains(withBreakers.get(j))) {
						connectNode.remove(index);
						index = -1;
						isBreaker = true;
					}
					j++;
				}
				index++;
			}

			// Create as many SCN as CN are left in the array
			for (int j = 0; j < connectNode.size(); j++) {
				String idCN = connectNode.get(j).get(0).getConnectNode();
				sCNList.add(new SuperConnectivityNode(idCN, connectNode.get(j)));
			}
			
			// Y matrix calculation
			YMatrix Y = new YMatrix(100, sCNList, lines, terminal, bVoltList, powtrafo, powtrafoEnd);
			ComplexNumber[][] solution=Y.getY();
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
