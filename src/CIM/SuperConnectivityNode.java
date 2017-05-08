package CIM;

import java.util.ArrayList;

public class SuperConnectivityNode {

	String id1, id2;
	ArrayList<String> terminalIdList= new ArrayList<String>();
	
	public SuperConnectivityNode (String id1, ArrayList<String> terminalIdList) {
		this.id1=id1;
		this.id2=id1;
		this.terminalIdList=terminalIdList;
	}
	
	public SuperConnectivityNode (String id1, String id2, ArrayList<String> terminalIdList) {
		this.id1=id1;
		this.id2=id2;
		this.terminalIdList=terminalIdList;
	}
	
	// constructor
		// lista de terminales
		// id de los dos CN fusionados
	// métodos
		// preguntar si un terminal pertenece
	
	public boolean isItIn(String terminalId) {
		for (int i=0; i<terminalIdList.size(); i++) {
			if (terminalId.equals(terminalIdList.get(i)))
				return true;
		}
		return false;
	}
}
