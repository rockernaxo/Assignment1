package CIM;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CircuitBreaker extends CIM {

	private boolean state;
	private String equipmentContainer;

	public CircuitBreaker(Node nodeEQ, NodeList listSSH) {
		super(nodeEQ);
		this.state = Boolean.parseBoolean(extractTag(extractFromId(listSSH), "cim:Switch.open"));
		this.equipmentContainer = extractAttFromTag(nodeEQ, "cim:Equipment.EquipmentContainer", "rdf:resource").substring(1);
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getEquipmentContainer() {
		return equipmentContainer;
	}

	public void setEquipmentContainer(String equipmentContainer) {
		this.equipmentContainer = equipmentContainer;
	}
}
