package model.rules;

import model.Box;
import model.ProofRow;
import model.VerificationInputException;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import start.Constants;

public class Assumption extends Rule {
    @Override
    public String toString() {
        return "assumption";
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
    public boolean verifyReferences(Box data, int rowIndex) {
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        ProofRow rowToVerify = data.getRow(rowIndex);
        if (rowToVerify.getFormula() instanceof FreshVarFormula)
            throw new VerificationInputException("A term is not a formula.");
        // Check if first row of box and box is an actual box.
        Box parent = rowToVerify.getParent();
        if (parent.isTopLevelBox())
            throw new VerificationInputException("Ass. needs to be in a box.");
        int index = parent.indexOf(rowToVerify);
        if( index == 0) return true;
        if( index == 1 && parent.getRow(0).getRule() instanceof FreshVar) return true;
        throw new VerificationInputException("Ass. needs to be first or after fresh in box.");
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }
    
    @Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
		return Constants.assumption;
	}
}
