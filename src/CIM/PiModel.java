package CIM;

// This interface is created to enable the use of a generic method in the calculation of the Y matrix
// Both transformers and lines use the Pi Model, this interface makes possible to use the same procedure 
// in determining the Y matrix coefficient for each line and transformer.

public interface PiModel {

	public String getRdfID();

	public String getBaseVoltage();

	public double getR();

	public double getX();

	public double getG();

	public double getB();
}
