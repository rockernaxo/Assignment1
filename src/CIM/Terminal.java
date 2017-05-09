package CIM;

import org.w3c.dom.Node;

public class Terminal extends CIM {
	
	private String condEquip, connectNode;

	public Terminal(Node nodeEQ) {
		super(nodeEQ);
		this.condEquip = extractAttFromTag(nodeEQ, "cim:Terminal.ConductingEquipment", "rdf:resource").substring(1);
		this.connectNode = extractAttFromTag(nodeEQ, "cim:Terminal.ConnectivityNode", "rdf:resource").substring(1);
	}

	public String getCondEquip() {
		return condEquip;
	}

	public String getConnectNode() {
		return connectNode;
	}
}