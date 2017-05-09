import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import CIM.*;
public class YMatrix {

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
		// TODO Auto-generated method stub
		//lista lines y lista terminal. L es la lista de SCN
		ArrayList<SuperConnectivityNode> sCNList = new ArrayList<SuperConnectivityNode>();
		
		double Sbase=1000; //MVA
		ComplexNumber zero = new ComplexNumber();		
		ComplexNumber[][] Y = new ComplexNumber[sCNList.size()][sCNList.size()];
		for (int r = 0; r < sCNList.size(); r++){
		  for (int c = 0; c < sCNList.size(); c++){
		      Y[r][c] = zero;
		  }
		}
		Boolean firstTerminal=false;
		Terminal T1, T2;
		for (int i = 0; i < lines.size(); i++) {
			double zbase=1;
			int CN1=-1;
			int CN2=-1;
			for (int j = 0; j < terminal.size(); j++) {
				 if (terminal.get(j).getRdfID().equals(lines.get(i).getRdfID())){
					if (!firstTerminal){
						firstTerminal=true;
						T1=terminal.get(j);	
						for (int n = 0; j < sCNList.size(); n++) {
							if (sCNList.get(n).getTerminalList().contains(T1)){
								CN1=n;
								}
							}
						}
					else{
						T2=terminal.get(j);
						for (int n = 0; j < sCNList.size(); n++) {
							if (sCNList.get(n).getTerminalList().contains(T2)){
								CN2=n;
								}
							}
						}					
					}
				}				
			 for (int k = 0; i < bVoltList.size(); k++) {
				 if (lines.get(i).getBaseVoltage().equals(bVoltList.get(k).getRdfID())){
					 zbase= Math.pow(bVoltList.get(k).getNominalValue(),2)/Sbase;					 				 
				 }
			ComplexNumber Z=new ComplexNumber(lines.get(i).getR()/zbase, lines.get(i).getX()/zbase );
			ComplexNumber y=new ComplexNumber(lines.get(i).getG()*zbase, lines.get(i).getB()*zbase );
			ComplexNumber Zy=ComplexNumber.add(Z,y);
			Z.inv();			
			Y[CN1][CN1].addTo(Zy);
			Y[CN2][CN2].addTo(Zy);
			Z.neg();
			Y[CN1][CN2]=Z;
			Y[CN2][CN1]=Z;
			 }
		}
		
		//We update all the transformers to supertrafos
		ArrayList<SuperTrafo> superTrafo = new ArrayList<SuperTrafo>();
		for (int i = 0; i < powtrafo.size(); i++) {
			SuperTrafo newSuperTrafo= new SuperTrafo(powtrafo.get(i), powtrafoEnd);
			superTrafo.add(newSuperTrafo);			
		}
		
		Boolean firstTerminalTrafo=false;
		for (int i = 0; i < superTrafo.size(); i++) {
			double zbase=1;
			int CN1=-1;
			int CN2=-1;
			for (int j = 0; j < terminal.size(); j++) {
				if (terminal.get(j).getRdfID().equals(superTrafo.get(i).getRdfID())){
					if (!firstTerminalTrafo){
						firstTerminalTrafo=true;
						T1=terminal.get(j);	
						for (int n = 0; j < sCNList.size(); n++) {
							if (sCNList.get(n).getTerminalList().contains(T1)){
								CN1=n;
								}
							}
						}
					else{
						T2=terminal.get(j);
						for (int n = 0; j < sCNList.size(); n++) {
							if (sCNList.get(n).getTerminalList().contains(T2)){
								CN2=n;
								}
							}
						}
					
				}
			
			}
			for (int k = 0; i < bVoltList.size(); k++) {
				 if (superTrafo.get(i).getTrafoEnd1().getBaseVoltage().equals(bVoltList.get(k).getRdfID())){
					 zbase= Math.pow(bVoltList.get(k).getNominalValue(),2)/Sbase;					 				 
				 }
			ComplexNumber Z=new ComplexNumber(superTrafo.get(i).getTrafoEnd1().getR()/zbase, superTrafo.get(i).getTrafoEnd1().getX()/zbase );
			ComplexNumber y=new ComplexNumber(superTrafo.get(i).getTrafoEnd1().getG()*zbase, superTrafo.get(i).getTrafoEnd1().getB()*zbase );
			ComplexNumber Zy=ComplexNumber.add(Z,y);
			Z.inv();			
			Y[CN1][CN1].addTo(Zy);
			Y[CN2][CN2].addTo(Zy);
			Z.neg();
			Y[CN1][CN2]=Z;
			Y[CN2][CN1]=Z;
			 }
		}
		
		/*ComplexNumber jordi = new ComplexNumber(2,5);
		jordi.neg();
		System.out.print(jordi);
		*/
		
		System.out.print("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

}
