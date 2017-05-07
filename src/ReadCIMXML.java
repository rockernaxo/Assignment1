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
				"cim:SynchronousMachine", "cim:GeneratingUnit", "cim:RegulatingControl", "cim:PowerTransformer", "cim:PowerTransformerEnd"
				,"cim:EnergyConsumer", "cim:RatioTapChanger", "cim:ACLineSegment", "cim:Terminal", "cim:BusbarSection", "cim:ConnectivityNode"};

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
			
			String idCN;
			String tCN;
			String tID;
			ArrayList<String> CNTerminal = new ArrayList<String>();
			Map<String, ArrayList<String>> nombreMap = new HashMap<String, ArrayList<String>>();
			Map<String, String> nombre = new HashMap<String, String>();
			for (int ii1 = 0; ii1 < connectivityNode.size(); ii1++) { 
				 idCN=connectivityNode.get(ii1).getRdfID();   //get id for CN
				 CNTerminal.clear();
				 
				for (int jj1 = 0; jj1 < terminal.size(); jj1++) {					
					tCN=terminal.get(jj1).getConnectNode();
					tID=terminal.get(jj1).getRdfID();
					//t = t.startsWith("#") ? t.substring(1) : t;	
					//System.out.println(t);
					//System.out.println(idbus);
					nombre.put(tID, tCN); //map with id terminal key CN
					if (tCN.equals(idCN)){	 //put the terminals that have these id in a list						
						CNTerminal.add(tID);
											}					
				}
				nombreMap.put(idCN,CNTerminal);				
			}
			
			ArrayList<String> lis = new ArrayList<String>();
			String tCNDE;
			String idBr;
			String previousT;
			String CN1 = null;
			String CN2 = null;
			for (int ii1 = 0; ii1 < breakerList.size(); ii1++) { 
				idBr=breakerList.get(ii1).getRdfID();   //get id for breaker
				previousT="";
				 
				for (int jj1 = 0; jj1 < terminal.size(); jj1++) {					
					tCNDE=terminal.get(jj1).getCondEquip();  
					//t = t.startsWith("#") ? t.substring(1) : t;	
					//System.out.println(t);
					//System.out.println(idbus);
				
					if (tCNDE.equals(idBr)){
						
						if (previousT==""){
							previousT=terminal.get(jj1).getRdfID();
						}
						else{
							CN1=nombre.get(previousT);
							CN2=nombre.get(terminal.get(jj1).getRdfID());
						}
						}
					
				}
				System.out.println(CN1 + "CN1");
				System.out.println(CN2 + "CN2");
				//lis.clear();
				//if (breakerList.get(ii1).isState()){
					//lis=nombreMap.get(CN1);
					//lis.addAll(nombreMap.get(CN2));
					//nombreMap.remove(CN1);
					//nombreMap.remove(CN2);
					//nombreMap.put(CN1, lis);
				//}				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			String idbus;
			String connter;
			//String jordi;
			String t;
			ArrayList<String> bustTerminal = new ArrayList<String>();
			for (int ii1 = 0; ii1 < busbarSection.size(); ii1++) {
				 idbus=busbarSection.get(ii1).getRdfID();
				 
				for (int jj1 = 0; jj1 < terminal.size(); jj1++) {					
					t=terminal.get(jj1).getCondEquip();
					//t = t.startsWith("#") ? t.substring(1) : t;	
					//System.out.println(t);
					//System.out.println(idbus);
					if (t.equals(idbus)){	
						connter=terminal.get(jj1).getConnectNode();
						bustTerminal.add(connter);
											}					
				}
			}
			String idline;
			String tt;	
			String ttt;	
			ArrayList<Double> busline = new ArrayList<Double>();
			ArrayList<ArrayList<Double>> buslineMaster = new  ArrayList<ArrayList<Double>>();
			
			for (int ii = 0; ii < lines.size(); ii++) { //Iteration in lines elements
				idline=lines.get(ii).getRdfID(); //take the id of line ii
				for (int jj = 0; jj < terminal.size(); jj++) {  //go through all terminals
					ttt=terminal.get(jj).getConnectNode();
					ttt= ttt.startsWith("#") ? ttt.substring(1) : ttt; //take cond equip i treure #
					tt=terminal.get(jj).getCondEquip();//take the cond equipment of terminal
					tt= tt.startsWith("#") ? tt.substring(1) : tt;
					if (idline.equals(tt)){
						//System.out.println(ttt);
						for (int jjj = 0; jjj < bustTerminal.size(); jjj++) { //go through the buses list of found above which is made of connect nodes id
							if (ttt.equals(bustTerminal.get(jjj))){
								busline.add((double)(jjj));							
								busline.add(lines.get(ii).getR());
								busline.add(lines.get(ii).getX());
								busline.add(lines.get(ii).getB());
							
							}
						}
					}
					buslineMaster.add(busline);
					
					}
				//busline.clear();
				}
				
			System.out.println(nombreMap);
			System.out.println(nombre);
			//System.out.println(buslineMaster);	
			System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
