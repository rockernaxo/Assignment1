package CIM;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SynchronousMachine extends CIM {

	private String genUnit, regControl, equipmentContainer;
	private double p, q, ratedS;

	public SynchronousMachine(Node nodeEQ, NodeList listSSH) {
		super(nodeEQ);
		this.p = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:RotatingMachine.p"));
		this.q = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:RotatingMachine.q"));
		this.ratedS = Double.parseDouble(extractTag(nodeEQ, "cim:RotatingMachine.ratedS"));
		this.genUnit = extractAttFromTag(nodeEQ, "cim:RotatingMachine.GeneratingUnit", "rdf:resource").substring(1);
		this.regControl = extractAttFromTag(nodeEQ, "cim:RegulatingCondEq.RegulatingControl", "rdf:resource")
				.substring(1);
		this.equipmentContainer = extractAttFromTag(nodeEQ, "cim:Equipment.EquipmentContainer", "rdf:resource")
				.substring(1);
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
