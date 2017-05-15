package CIM;

import org.w3c.dom.Node;

public class GeneratingUnit extends CIM {

	private double Pmax, Pmin;
	private String equipmentContainer;

	public GeneratingUnit(Node node) {
		super(node);
		this.Pmax = Double.parseDouble(extractTag(node, "cim:GeneratingUnit.maxOperatingP"));
		this.Pmin = Double.parseDouble(extractTag(node, "cim:GeneratingUnit.minOperatingP"));
		this.equipmentContainer = extractAttFromTag(node, "cim:Equipment.EquipmentContainer", "rdf:resource")
				.substring(1);
	}

	public double getPmax() {
		return Pmax;
	}

	public double getPmin() {
		return Pmin;
	}

	public String getEquipmentContainer() {
		return equipmentContainer;
	}

}
