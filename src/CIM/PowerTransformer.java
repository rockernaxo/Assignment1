package CIM;
import org.w3c.dom.Node;
public class PowerTransformer extends CIM {
	private String equipmentContainer;
	public PowerTransformer(Node node) {
		super(node);
		this.equipmentContainer = extractAttFromTag(node, "cim:Equipment.EquipmentContainer", "rdf:resource");
	}
	public String getEquipmentContainer() {
		return equipmentContainer;
	}
}
