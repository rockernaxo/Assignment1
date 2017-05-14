package CIM;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RatioTapChanger extends CIM {
	private double step;

	public RatioTapChanger(Node nodeEQ, NodeList listSSH) {
		super(nodeEQ);
		this.step = Double.parseDouble(extractTag(extractFromId(listSSH), "cim:TapChanger.step"));
	}

	public double getStep() {
		return step;
	}

}
