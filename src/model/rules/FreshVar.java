package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;

public class FreshVar implements Rule{

	@Override
	public boolean hasCompleteInfo() {
		return true;
	}

	@Override
	public void updateReference(int index, String newValue) {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		ProofRow row = data.getRow(rowIndex);
		//check that the rule is the first row in the box
		if(row.getParent().getRow(0) != row) return false;
		//check that it's the correct type of formula
		if(row.getFormula() instanceof FreshVarFormula == false) return false;
		//TODO: check that the var is actually fresh!
		return true;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		return null;
	}

	@Override
	public String toString(){
		return "Fresh var.";
	}

}
