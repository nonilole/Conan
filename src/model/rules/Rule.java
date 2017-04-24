package model.rules;

import java.io.Serializable;

import model.Box;
import model.formulas.Formula;

public interface Rule extends Serializable{
	
	//if this is false there is no reason to continue the verification
	//since info is missing
	public boolean hasCompleteInfo();
	//Update the reference in the rule object
	public void updateReference(int index, String newValue);
	public boolean verify(Box data, int rowIndex);
	public Formula generateFormula(Box data, int rowIndex);
	public String[] getReferenceStrings();
	public String getDisplayName();
}
