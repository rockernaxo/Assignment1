package CIM;
import org.w3c.dom.Node;
public class RatioTapChanger extends CIM{
	private double step;
	public RatioTapChanger(Node nodeEQ, Node nodeSSH) {
		super(nodeEQ);
		this.step = Double.parseDouble(extractTag(nodeSSH, "cim:TapChanger.step"));
	}
	public double getStep() {
		return step;
	}

}


