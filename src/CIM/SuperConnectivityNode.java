package CIM;

import java.util.ArrayList;

public class SuperConnectivityNode {

	private ArrayList<String> idCN = new ArrayList<String>();
	private ArrayList<Terminal> terminalList = new ArrayList<Terminal>();

	public SuperConnectivityNode(String idCN1, ArrayList<Terminal> terminalList) {
		this.idCN.add(idCN1);
		this.terminalList = terminalList;
	}
	
	public SuperConnectivityNode(String idCN1, String idCN2, ArrayList<Terminal> terminalList) {
		this.idCN.add(idCN1);
		this.idCN.add(idCN2);
		this.terminalList = terminalList;
	}

	public ArrayList<Terminal> getTerminalList() {
		return terminalList;
	}

	public ArrayList<String> getIdCN() {
		return idCN;
	}

	public void addId(ArrayList<String> idCN) {
		// Remove duplicates
		this.idCN.removeAll(idCN);
		// Add the new ones
		this.idCN.addAll(idCN);
	}

	public void mergeTerminalLists(ArrayList<Terminal> terminalList) {
		// Remove duplicates
		this.terminalList.removeAll(terminalList);
		// Add new terminals to the list
		this.terminalList.addAll(terminalList);
	}
	
	public int isItIn(String terminalId) {
		for (int i = 0; i < terminalList.size(); i++) {
			if (terminalId.equals(terminalList.get(i).getRdfID()))
				return i;
		}
		return -1;
	}
}
