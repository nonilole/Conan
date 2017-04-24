package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;

public class Assumption implements Rule {
    @Override
    public String toString() {
        return "Assumption";
    }

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
        ProofRow rowToVerify = data.getRow(rowIndex);
        // Check if first row of box and box is an actual box.
        Box parent = rowToVerify.getParent();
        if (parent.isTopLevelBox())
            return false;
        return parent.getRow(0).equals(rowToVerify);
    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        return null;
    }
    
    @Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
		return "Ass.";
	}
}
