package CIM;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EnergyConsumer extends CIM {
	private double p, q;
	private String equipmentContainer;

	public EnergyConsumer(Node nodeEQ, NodeList listSSH) {
		super(nodeEQ);
		this.p = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:EnergyConsumer.p"));
		this.q = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:EnergyConsumer.q"));
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
