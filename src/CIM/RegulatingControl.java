package CIM;
import org.w3c.dom.Node;
public class RegulatingControl extends CIM {
	private double targetValue;
	public RegulatingControl(Node nodeEQ, Node nodeSSH) {
		super(nodeEQ);
		this.targetValue = Double.parseDouble(extractTag(nodeSSH, "cim:RegulatingControl.targetValue"));
	}
	public double getTargetValue() {
		return targetValue;
	}

}
