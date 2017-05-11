import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import CIM.*;

public class YMatrix {

	private ArrayList<SuperConnectivityNode> sCNList;
	private ArrayList<Lines> lines;
	private ArrayList<Terminal> terminal;
	private ArrayList<BaseVoltage> bVoltList;
	private ArrayList<PowerTransformer> powTrans;
	private ArrayList<PowerTransformerEnd> powtrafoEnd;
	private int sBase;
	private ComplexNumber[][] Y;
	
	public YMatrix (int sBase, ArrayList<SuperConnectivityNode> sCNList, 
			ArrayList<Lines> lines, ArrayList<Terminal> terminal, ArrayList<BaseVoltage> bVoltList,
			ArrayList<PowerTransformer> powTrans, ArrayList<PowerTransformerEnd> powtrafoEnd) {
		this.sCNList=sCNList;
		this.lines=lines;
		this.terminal=terminal;
		this.bVoltList=bVoltList;
		this.powTrans=powTrans;
		this.powtrafoEnd=powtrafoEnd;
		this.sBase=sBase;
		initialize();
		calculate();
	}
	
	public void calculate() {
			
		iteration(this.lines);
		
		// Update all the transformers to supertrafos
		ArrayList<SuperTrafo> superTrafo = new ArrayList<SuperTrafo>();
		for (int i = 0; i < powTrans.size(); i++) {
			SuperTrafo newSuperTrafo= new SuperTrafo(powTrans.get(i), powtrafoEnd);
			superTrafo.add(newSuperTrafo);			
		}
		
		iteration(superTrafo);
	}
	
	private void initialize () {
		// Initialize the Y matrix with all 0s
		Y = new ComplexNumber[sCNList.size()][sCNList.size()];
		for (int r = 0; r < sCNList.size(); r++){
			for (int c = 0; c < sCNList.size(); c++){
				Y[r][c] = new ComplexNumber();
			}
		}
	}
	
	
	private <T extends PiModel> void iteration (ArrayList<T> iteration) {
		
		// Iterate over the objects in the array. Either lines or transformers
		for (int i = 0; i < iteration.size(); i++) {
			Boolean firstTerminal=false;
			Terminal T1=null;
			Terminal T2=null;
			int CN1=-1;
			int CN2=-1;
			double zBase=1;

			// Get the SCN of the element terminals
			for (int j = 0; j < this.terminal.size(); j++) {
				if (this.terminal.get(j).getCondEquip().equals(iteration.get(i).getRdfID())){
					if (!firstTerminal){
						firstTerminal=true;
						T1=this.terminal.get(j);	
					} else {
						T2=terminal.get(j);
						for (int n = 0; n < sCNList.size(); n++) {
							if (sCNList.get(n).getTerminalList().contains(T1)){
								CN1=n;
								}
							if (sCNList.get(n).getTerminalList().contains(T2)){
								CN2=n;
							}
						}
					}				
				}
			}
			
			if (CN1!=-1 && CN2!=-1) {
			
				// Get the base impedance of that element
				for (int k = 0; k < this.bVoltList.size(); k++) {
					if (iteration.get(i).getBaseVoltage().equals(this.bVoltList.get(k).getRdfID())){
						 zBase= Math.pow(this.bVoltList.get(k).getNominalValue(),2)/this.sBase;					 				 
					}
				}
				
				// Add the elements to the Y matrix
				// Pi model is used
				// Get the impendance of the element
				ComplexNumber busImp=new ComplexNumber(iteration.get(i).getR()/zBase, iteration.get(i).getX()/zBase );
				// Get the inverse 
				busImp.inv();
				// Get the admittance of the element from the susceptance and conductance
				ComplexNumber y=new ComplexNumber(iteration.get(i).getG()*zBase, iteration.get(i).getB()*zBase );
				// Only half of the admittance belongs to the bus
				y.divide(new ComplexNumber(2,1));			
				// Combine both into a complex number (admittance)
				ComplexNumber busAd=ComplexNumber.add(busImp,y);
				
				addToMatrix(busAd, busImp, CN1, CN2);
			}
		}
	}
	
	private void addToMatrix(ComplexNumber busAd, ComplexNumber busImp, int CN1, int CN2) {
		// Add the values to the respective places in the matrix
		// Add diagonal elements
		this.Y[CN1][CN1].addTo(busAd);
		this.Y[CN2][CN2].addTo(busAd);
		// Non-diagonal elements will be negative
		busImp.neg();
		this.Y[CN1][CN2].addTo(busImp);
		this.Y[CN2][CN1].addTo(busImp);
	}
	
	public ComplexNumber[][] getY() {
		return Y;
	}

}
