package CIM;

import org.w3c.dom.Node;

public class Substation extends CIM {

	private String region;

	public Substation(Node node) {
		super(node);
		this.name=extractTag(node, "cim:IdentifiedObject.name");
		this.region = extractAttFromTag(node, "cim:Substation.Region", "rdf:resource");
	}

	public String getRegion() {
		return region;
	}
}
