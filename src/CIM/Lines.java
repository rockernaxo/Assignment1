package CIM;
import org.w3c.dom.Node;
public class Lines extends CIM implements PiModel{
	private double r, x, b, g;
	private String baseVoltage;
	public Lines(Node nodeEQ) {
		super(nodeEQ);
		this.r = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.r"));
		this.x = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.x"));
		this.b = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.bch"));
		this.g = Double.parseDouble(extractTag(nodeEQ, "cim:ACLineSegment.gch"));		
		this.baseVoltage = extractAttFromTag(nodeEQ, "cim:ConductingEquipment.BaseVoltage", "rdf:resource").substring(1);
	}
	

	public String getBaseVoltage() {
		return baseVoltage;
	}

	public double getG() {
		return g;
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

