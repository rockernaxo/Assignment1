package CIM;
import org.w3c.dom.Node;

public class BaseVoltage extends CIM {

	private double nominalValue;

	public BaseVoltage(Node node) {
		super(node);
		this.nominalValue = Double.parseDouble(extractTag(node, "cim:BaseVoltage.nominalVoltage"));
	}

	public double getNominalValue() {
		return nominalValue;
	}
}
