package model.rules;

import model.Box;
import model.formulas.Formula;
import start.Constants;

public class Copy extends Rule{
	private Integer rowRef;
	
	@Override
	public boolean hasCompleteInfo() {
		return rowRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if (index != 1) throw new IllegalArgumentException();
        try {
            rowRef = ReferenceParser.parseIntegerReference(newValue);
        } catch (NumberFormatException e) {
            rowRef = null;
            throw new NumberFormatException();
        }
	}

	@Override
	public boolean verifyReferences(Box data, int rowIndex) {
		return data.isInScopeOf(rowRef, rowIndex);
	}

	@Override
	public boolean verifyRow(Box data, int rowIndex) {
		return data.getRow(rowRef).getFormula().equals(data.getRow(rowIndex).getFormula());
	}

	@Override
	public Formula generateRow(Box data) {
		return data.getRow(rowRef).getFormula();
	}

	@Override
	public String[] getReferenceStrings() {
		return new String[]{(rowRef+1)+""};
	}

	@Override
	public String getDisplayName() {
		return Constants.copy;
	}
	
	@Override
	public String toString(){
		return String.format("copy, %s", rowRef == null ? "" : new Integer(rowRef+1));
	}

}
