import java.util.ArrayList;

import CIM.*;

public class TopologyProcessor {

	private ArrayList<ArrayList<Terminal>> connectNode;
	private ArrayList<SuperConnectivityNode> sCNList;
	private ArrayList<ConnectivityNode> cNList;
	private ArrayList<CircuitBreaker> breakerList;
	private ArrayList<Terminal> terminal;
	private ArrayList<Terminal> withBreakers;

	public TopologyProcessor(ArrayList<ConnectivityNode> cNList, ArrayList<Terminal> terminal,
			ArrayList<CircuitBreaker> breakerList) {
		this.connectNode = new ArrayList<ArrayList<Terminal>>();
		this.sCNList = new ArrayList<SuperConnectivityNode>();
		// List of the idCN of CN with breakers
		withBreakers = new ArrayList<Terminal>();
		this.cNList = cNList;
		this.terminal = terminal;
		this.breakerList = breakerList;
		process();
	}

	private void process() {

		// Nested arrayList containing all the terminals of a certain CN. It
		// will be used later on.
		// Iterate over the CN to create the matrix
		for (int i = 0; i < cNList.size(); i++) {
			// Add a new terminal list to the matrix
			this.connectNode.add(new ArrayList<Terminal>());
			// Add all the terminals with the same idCN to a list
			for (Terminal terminal : this.terminal) {
				if (terminal.getConnectNode().equals(this.cNList.get(i).getRdfID())) {
					this.connectNode.get(i).add(terminal);
				}
			}
		}

		// Now we iterate over the breakers to create the SuperConnectivityNodes
		for (int i = 0; i < this.breakerList.size(); i++) {
			// Check if is the first or the second terminal of the breaker
			boolean firstTerminal = false;
			// Variable to keep the id
			int firstCN = -1;
			// We create a list in which we will include all the terminals
			// aggregated into the SCN
			ArrayList<Terminal> terminalList = new ArrayList<Terminal>();

			boolean isBreaker = false;

			// Look for the terminals in the this.connectNode list
			for (int j = 0; j < this.connectNode.size(); j++) {
				// Look in the list of terminals for the breaker's
				for (int k = 0; k < this.connectNode.get(j).size(); k++) {
					// We find the first terminal of the breaker
					if (this.connectNode.get(j).get(k).getCondEquip().equals(this.breakerList.get(i).getRdfID())) {
						// Activate the variable for the non-breaker list
						isBreaker = true;
						// Check if we found only the first terminal
						if (!firstTerminal) {
							// We have found the first terminal
							terminalList.addAll(this.connectNode.get(j));
							firstTerminal = true;
							firstCN = j;
						} else {
							// The second is found and the SCN can be
							// created
							terminalList.addAll(this.connectNode.get(j));
							if (!this.breakerList.get(i).isState()) {
								this.sCNList.add(
										new SuperConnectivityNode(this.connectNode.get(firstCN).get(0).getConnectNode(),
												this.connectNode.get(j).get(k).getConnectNode(), terminalList));
							}
						}
					}
				}

				// Store CN with breakers into a list
				if (isBreaker) {
					isBreaker = false;
					this.withBreakers.add(this.connectNode.get(j).get(0));
				}
			}
		}

		// Check for duplicate SCNs in the SCN list.
		duplicates();

		// All the CN without any breakers are directly a SCN.
		// Remove all CN with breakers from the matrix created previously.
		int index = 0;
		while (index < this.connectNode.size()) {
			int j = 0;
			boolean isBreaker = false;
			while (j < this.withBreakers.size() && !isBreaker) {
				if (this.connectNode.get(index).contains(this.withBreakers.get(j))) {
					this.connectNode.remove(index);
					index = -1;
					isBreaker = true;
				}
				j++;
			}
			index++;
		}

		// Create as many SCN as CN are left in the matrix
		for (ArrayList<Terminal> sCN : this.connectNode) {
			String idCN = sCN.get(0).getConnectNode();
			this.sCNList.add(new SuperConnectivityNode(idCN, sCN));
		}
	}

	// This method check for duplicates in the SCN list.
	private void duplicates() {
		for (int i = 0; i < this.sCNList.size(); i++) {
			// Get one SCN to compare it with others
			for (int k = 0; k < this.sCNList.get(i).getIdCN().size(); k++) {
				// Get the idCN from the SCN one by one
				String idCN = this.sCNList.get(i).getIdCN().get(k);
				for (int j = i + 1; j < this.sCNList.size(); j++) {
					if (this.sCNList.get(j).getIdCN().contains(idCN)) {
						// If the SCN have a CN in common, they are electrically
						// equal and they must be fused. The idCN and the
						// terminal lists are combined into one of them, the
						// other is removed.
						sCNList.get(i).addId(sCNList.get(j).getIdCN());
						sCNList.get(i).mergeTerminalLists(sCNList.get(j).getTerminalList());
						sCNList.remove(j);
					}
				}
			}
		}
	}

	public ArrayList<SuperConnectivityNode> getsCNList() {
		return sCNList;
	}
}
