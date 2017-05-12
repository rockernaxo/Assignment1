package CIM;

import org.w3c.dom.Node;

public class EnergyConsumer extends CIM {
	private double p, q;
	private String equipmentContainer;

	public EnergyConsumer(Node nodeEQ, Node nodeSSH) {
		super(nodeEQ);
		this.p = Double.parseDouble(extractTag(nodeSSH, "cim:EnergyConsumer.p"));
		this.q = Double.parseDouble(extractTag(nodeSSH, "cim:EnergyConsumer.q"));
		this.equipmentContainer = extractAttFromTag(nodeEQ, "cim:Equipment.EquipmentContainer", "rdf:resource")
				.substring(1);
	}

	public double getP() {
		return p;
	}

	public double getQ() {
		return q;
	}

	public String getEquipmentContainer() {
		return equipmentContainer;
	}
}
