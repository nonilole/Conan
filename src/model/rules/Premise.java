package model.rules;

import model.Box;

public class Premise implements Rule {
	
	
	
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
}
