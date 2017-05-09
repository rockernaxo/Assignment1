package CIM;

import java.util.ArrayList;

public class SuperTrafo implements PiModel{
	
	private PowerTransformerEnd trafoEnd1;
	String rdfID;

	public SuperTrafo(PowerTransformer trafo, ArrayList<PowerTransformerEnd> powtrafoEnd) {
		this.rdfID = trafo.getRdfID();
		for (int i = 0; i < powtrafoEnd.size(); i++) {
			if (powtrafoEnd.get(i).getPowerTransformer().equals(trafo.getRdfID())) {
				if (powtrafoEnd.get(i).getX() != 0)
					this.trafoEnd1 = (powtrafoEnd.get(i));
			}
		}
	}

	public PowerTransformerEnd getTrafoEnd1() {
		return trafoEnd1;
	}

	public String getRdfID() {
		return this.rdfID;
	}
	
	public String getBaseVoltage() {
		return this.trafoEnd1.getBaseVoltage();
	}
	
	public double getG() {
		return this.trafoEnd1.getG();
	}
	public double getR() {
		return this.trafoEnd1.getR();
	}
	public double getX() {
		return this.trafoEnd1.getX();
	}
	public double getB() {
		return this.trafoEnd1.getB();
	}

}
