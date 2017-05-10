package CIM;

import org.w3c.dom.Node;

public class VoltageLevel extends CIM {

	private String substation, baseVoltage;

	public VoltageLevel(Node node) {
		super(node);
		this.substation = extractAttFromTag(node, "cim:VoltageLevel.Substation", "rdf:resource").substring(1);
		this.baseVoltage = extractAttFromTag(node, "cim:VoltageLevel.BaseVoltage", "rdf:resource").substring(1);
	}

	public String getSubstation() {
		return substation;
	}

	public String getBaseVoltage() {
		return baseVoltage;
	}

}
