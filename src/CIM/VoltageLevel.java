package CIM;

import org.w3c.dom.Node;

public class VoltageLevel extends CIM {

	private String substation, baseVoltage;

	public VoltageLevel(Node node) {
		super(node);
		this.substation = extractAttFromTag(node, "cim:VoltageLevel.Substation", "rdf:resource");
		this.baseVoltage = extractAttFromTag(node, "cim:VoltageLevel.BaseVoltage", "rdf:resource");
	}

	public String getSubstation() {
		return substation;
	}

	public String getBaseVoltage() {
		return baseVoltage;
	}

}
