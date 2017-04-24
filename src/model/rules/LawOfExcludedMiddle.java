package model.rules;

import model.Box;
import model.formulas.Formula;

public class LawOfExcludedMiddle extends Rule {

	@Override
	public boolean hasCompleteInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateReference(int index, String newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verifyReferences(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRow(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Formula generateRow(Box data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getReferenceStrings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

}
