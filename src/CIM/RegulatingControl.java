package CIM;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RegulatingControl extends CIM {
	private double targetValue;

	public RegulatingControl(Node nodeEQ, NodeList listSSH) {
		super(nodeEQ);
		this.targetValue = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:RegulatingControl.targetValue"));
	}

	public double getTargetValue() {
		return targetValue;
	}

}
