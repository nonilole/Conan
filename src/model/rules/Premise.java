package model.rules;

import model.Box;
import model.formulas.Formula;

public class Premise implements Rule {
	
	@Override
	public void updateReference(int a, String b){
	  throw new IllegalArgumentException();
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return true;
	}

	@Override
	public String toString(){
		return "Premise";
	}

	@Override
    public boolean verify(Box data, int rowIndex) {
	    return true;
    }

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		return null;
	}
}
