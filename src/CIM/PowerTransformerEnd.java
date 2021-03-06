package CIM;

import org.w3c.dom.Node;

public class PowerTransformerEnd extends CIM {
	private String baseVoltage, powerTransformer;
	private double r, x, b, g;

	public PowerTransformerEnd(Node node) {
		super(node);
		this.r = Double.parseDouble(extractTag(node, "cim:PowerTransformerEnd.r"));
		this.x = Double.parseDouble(extractTag(node, "cim:PowerTransformerEnd.x"));
		this.b = Double.parseDouble(extractTag(node, "cim:PowerTransformerEnd.b"));
		this.g = Double.parseDouble(extractTag(node, "cim:PowerTransformerEnd.g"));
		this.baseVoltage = extractAttFromTag(node, "cim:TransformerEnd.BaseVoltage", "rdf:resource").substring(1);
		this.powerTransformer = extractAttFromTag(node, "cim:PowerTransformerEnd.PowerTransformer", "rdf:resource")
				.substring(1);
	}

	public double getB() {
		return b;
	}

	public double getG() {
		return g;
	}

	public String getBaseVoltage() {
		return this.baseVoltage;
	}

	public String getPowerTransformer() {
		return this.powerTransformer;
	}

	public double getR() {
		return r;
	}

	public double getX() {
		return x;
	}

}
