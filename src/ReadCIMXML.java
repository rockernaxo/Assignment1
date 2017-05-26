import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import CIM.*;

public class ReadCIMXML {

	private final String[] tags = { "cim:Breaker", "cim:BaseVoltage", "cim:VoltageLevel", "cim:Substation",
			"cim:SynchronousMachine", "cim:GeneratingUnit", "cim:RegulatingControl", "cim:PowerTransformer",
			"cim:PowerTransformerEnd", "cim:EnergyConsumer", "cim:RatioTapChanger", "cim:ACLineSegment", "cim:Terminal",
			"cim:BusbarSection", "cim:ConnectivityNode" };
	private ArrayList<NodeList> container, containerSSH;
	private ArrayList<CircuitBreaker> breakerList;
	private ArrayList<BaseVoltage> bVoltList;
	private ArrayList<Terminal> terminal;
	private ArrayList<ConnectivityNode> connectivityNode;
	private ArrayList<PowerTransformerEnd> powtrafoEnd;
	private ArrayList<Lines> lines;
	private ArrayList<PowerTransformer> powtrafo;

	ArrayList<Substation> subList = new ArrayList<Substation>();
	ArrayList<VoltageLevel> voltLvlList = new ArrayList<VoltageLevel>();
	ArrayList<SynchronousMachine> synMach = new ArrayList<SynchronousMachine>();
	ArrayList<GeneratingUnit> genUnit = new ArrayList<GeneratingUnit>();
	ArrayList<RegulatingControl> regControl = new ArrayList<RegulatingControl>();
	ArrayList<EnergyConsumer> energCons = new ArrayList<EnergyConsumer>();
	ArrayList<RatioTapChanger> ratiotap = new ArrayList<RatioTapChanger>();
	ArrayList<BusbarSection> busbarSection = new ArrayList<BusbarSection>();

	public ReadCIMXML(File fileEQ, File fileSSH) {
		this.container = new ArrayList<NodeList>();
		this.containerSSH = new ArrayList<NodeList>();
		this.bVoltList = new ArrayList<BaseVoltage>();
		this.breakerList = new ArrayList<CircuitBreaker>();
		this.terminal = new ArrayList<Terminal>();
		this.connectivityNode = new ArrayList<ConnectivityNode>();
		this.lines = new ArrayList<Lines>();
		this.powtrafo = new ArrayList<PowerTransformer>();
		this.powtrafoEnd = new ArrayList<PowerTransformerEnd>();

		processXML(fileEQ, fileSSH);
	}

	// Main method to process the CIM XML files
	private void processXML(File fileEQ, File fileSSH) {
		try {

			// Create and initiate the XML parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document docEQ = dBuilder.parse(fileEQ);
			Document docSSH = dBuilder.parse(fileSSH);

			// Normalize the CIM XML File
			docEQ.getDocumentElement().normalize();
			docSSH.getDocumentElement().normalize();

			// Add all NodeLists from the XML file into an array extracting the
			// tags that are of interest
			containerSSH.add(docSSH.getElementsByTagName("cim:Breaker"));
			containerSSH.add(docSSH.getElementsByTagName("cim:SynchronousMachine"));
			containerSSH.add(docSSH.getElementsByTagName("cim:RegulatingControl"));
			containerSSH.add(docSSH.getElementsByTagName("cim:EnergyConsumer"));
			containerSSH.add(docSSH.getElementsByTagName("cim:RatioTapChanger"));
			for (int i = 0; i < tags.length; i++) {
				container.add(docEQ.getElementsByTagName(tags[i]));
			}

			// Iterate over each list to create the database.
			for (NodeList nodeList : container) {

				for (int j = 0; j < nodeList.getLength(); j++) {
					// Add a new object to the ArrayList
					String selec = nodeList.item(j).getNodeName();
					switch (selec) {
					case "cim:Breaker":
						breakerList.add(new CircuitBreaker(nodeList.item(j), containerSSH.get(0)));
						break;
					case "cim:BaseVoltage":
						bVoltList.add(new BaseVoltage(nodeList.item(j)));
						break;
					case "cim:Substation":
						subList.add(new Substation(nodeList.item(j)));
						break;
					case "cim:VoltageLevel":
						voltLvlList.add(new VoltageLevel(nodeList.item(j)));
						break;
					case "cim:SynchronousMachine":
						synMach.add(new SynchronousMachine(nodeList.item(j), containerSSH.get(1)));
						break;
					case "cim:GeneratingUnit":
						genUnit.add(new GeneratingUnit(nodeList.item(j)));
						break;
					case "cim:RegulatingControl":
						regControl.add(new RegulatingControl(nodeList.item(j), containerSSH.get(2)));
						break;
					case "cim:PowerTransformer":
						powtrafo.add(new PowerTransformer(nodeList.item(j)));
						break;
					case "cim:PowerTransformerEnd":
						powtrafoEnd.add(new PowerTransformerEnd(nodeList.item(j)));
						break;
					case "cim:EnergyConsumer":
						energCons.add(new EnergyConsumer(nodeList.item(j), containerSSH.get(3)));
						break;
					case "cim:RatioTapChanger":
						ratiotap.add(new RatioTapChanger(nodeList.item(j), containerSSH.get(4)));
						break;
					case "cim:ACLineSegment":
						lines.add(new Lines(nodeList.item(j)));
						break;
					case "cim:Terminal":
						terminal.add(new Terminal(nodeList.item(j)));
						break;
					case "cim:BusbarSection":
						busbarSection.add(new BusbarSection(nodeList.item(j)));
						break;
					case "cim:ConnectivityNode":
						connectivityNode.add(new ConnectivityNode(nodeList.item(j)));
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Something happened with the parsing, are the files OK?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<CircuitBreaker> getBreakerList() {
		return breakerList;
	}

	public ArrayList<BaseVoltage> getbVoltList() {
		return bVoltList;
	}

	public ArrayList<Terminal> getTerminal() {
		return terminal;
	}

	public ArrayList<ConnectivityNode> getConnectivityNode() {
		return connectivityNode;
	}

	public ArrayList<PowerTransformerEnd> getPowtrafoEnd() {
		return powtrafoEnd;
	}

	public ArrayList<Lines> getLines() {
		return lines;
	}

	public ArrayList<PowerTransformer> getPowtrafo() {
		return powtrafo;
	}

	public ArrayList<Substation> getSubList() {
		return subList;
	}

	public ArrayList<VoltageLevel> getVoltLvlList() {
		return voltLvlList;
	}

	public ArrayList<SynchronousMachine> getSynMach() {
		return synMach;
	}

	public ArrayList<GeneratingUnit> getGenUnit() {
		return genUnit;
	}

	public ArrayList<RegulatingControl> getRegControl() {
		return regControl;
	}

	public ArrayList<EnergyConsumer> getEnergCons() {
		return energCons;
	}

	public ArrayList<RatioTapChanger> getRatiotap() {
		return ratiotap;
	}

	public ArrayList<BusbarSection> getBusbarSection() {
		return busbarSection;
	}
}
