package model.rules;

import model.Box;
import model.formulas.Formula;

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
		//maybe check that this is first row in parent box
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
