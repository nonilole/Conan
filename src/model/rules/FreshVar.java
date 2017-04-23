package model.rules;

import model.Box;

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
	public String toString(){
		return "Fresh var.";
	}

}
