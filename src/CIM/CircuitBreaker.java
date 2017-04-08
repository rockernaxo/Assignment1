package CIM;
import org.w3c.dom.Node;

public class CircuitBreaker extends CIM {

	private boolean state;
	private String equipmentContainer;

	public CircuitBreaker(Node nodeEQ, Node nodeSSH) {
		super(nodeEQ);
		this.state = Boolean.parseBoolean(extractTag(nodeSSH, "cim:Switch.open"));
		this.name=extractTag(nodeEQ, "cim:IdentifiedObject.name");
		this.equipmentContainer = extractAttFromTag(nodeEQ, "cim:Equipment.EquipmentContainer", "rdf:resource");
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
