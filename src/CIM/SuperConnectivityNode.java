package CIM;

import java.util.ArrayList;

// This class is used to deal with the breakers. A SuperConnectivityNode (SCN) is a fusion of two ConnectivityNodes 
// when the breaker is closed. All the terminals in both sides of the breaker are aggregated into a SCN.
// The Y Matrix is constructed iterating over the SCNs, so CN (busbars) without breakers are also SCNs, hence
// the constructor with only one id.

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

	// This method is used in the fusion of two SCNs,
	// it adds the new CN id to the SCN.
	public void addId(ArrayList<String> idCN) {
		// Remove duplicate ids
		this.idCN.removeAll(idCN);
		// Add the new ones
		this.idCN.addAll(idCN);
	}

	// This method is used in the fusion of two SCNs, it adds the terminals
	// which are not in one
	// SCN to the other and remove the duplicates.
	public void mergeTerminalLists(ArrayList<Terminal> terminalList) {
		// Remove duplicates
		this.terminalList.removeAll(terminalList);
		// Add new terminals to the list
		this.terminalList.addAll(terminalList);
	}

	// Method to check if a certain terminal is in a SCN.
	public int isItIn(String terminalId) {
		for (int i = 0; i < terminalList.size(); i++) {
			if (terminalId.equals(terminalList.get(i).getRdfID()))
				return i;
		}
		return -1;
	}
}
