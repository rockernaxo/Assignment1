package CIM;
import org.w3c.dom.Node;
public class Lines extends CIM{
	private double r, x, b;
	public Lines(Node nodeEQ) {
		super(nodeEQ);
		this.r = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.r"));
		this.x = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.x"));
		this.b = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.bch"));
	}
	public double getR() {
		return r;
	}
	public double getX() {
		return x;
	}
	public double getB() {
		return b;
	}
}

