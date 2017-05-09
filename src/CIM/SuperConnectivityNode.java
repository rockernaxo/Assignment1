package CIM;

import java.util.ArrayList;

public class SuperConnectivityNode {

	private String idCN1, idCN2;
	private ArrayList<Terminal> terminalList = new ArrayList<Terminal>();

	// When the CN has no breakers
	public SuperConnectivityNode(String id1, ArrayList<Terminal> terminalList) {
		this.idCN1 = id1;
		this.idCN2 = id1;
		this.terminalList = terminalList;
	}

	public SuperConnectivityNode(String id1, String id2, ArrayList<Terminal> terminalList) {
		this.idCN1 = id1;
		this.idCN2 = id2;
		this.terminalList = terminalList;
	}

	public ArrayList<Terminal> getTerminalList() {
		return terminalList;
	}

	public int isItIn(String terminalId) {
		for (int i = 0; i < terminalList.size(); i++) {
			if (terminalId.equals(terminalList.get(i).getRdfID()))
				return i;
		}
		return -1;
	}
}
