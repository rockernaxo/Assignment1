package CIM;

import org.w3c.dom.Node;

public class SynchronousMachine extends CIM {

	private String genUnit, regControl, equipmentContainer;
	private double p, q, ratedS;

	public SynchronousMachine(Node nodeEQ, Node nodeSSH) {
		super(nodeEQ);
		this.p = Double.parseDouble(extractTag(nodeSSH, "cim:RotatingMachine.p"));
		this.q = Double.parseDouble(extractTag(nodeSSH, "cim:RotatingMachine.q"));
		this.ratedS = Double.parseDouble(extractTag(nodeEQ, "cim:RotatingMachine.ratedS"));
		this.genUnit = extractAttFromTag(nodeEQ, "cim:RotatingMachine.GeneratingUnit", "rdf:resource");
		this.regControl = extractAttFromTag(nodeEQ, "cim:RegulatingCondEq.RegulatingControl", "rdf:resource");
		this.equipmentContainer = extractAttFromTag(nodeEQ, "cim:Equipment.EquipmentContainer", "rdf:resource");
	}

	public String getGenUnit() {
		return genUnit;
	}

	public String getRegControl() {
		return regControl;
	}

	public String getEquipmentContainer() {
		return equipmentContainer;
	}

	public double getP() {
		return p;
	}

	public double getQ() {
		return q;
	}

	public double getRatedS() {
		return ratedS;
	}
}
