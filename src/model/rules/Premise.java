package model.rules;

import model.Box;
import model.VerificationInputException;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import start.Constants;

public class Premise extends Rule {

    @Override
    public void updateReference(int a, String b) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
    	if( data.getRow(rowIndex).getFormula() instanceof FreshVarFormula ) throw new VerificationInputException("A term is not a formula.");
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public String toString() {
        return "premise";
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
        return Constants.premise;
	}
}
