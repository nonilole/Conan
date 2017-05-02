package model.rules;

import model.Box;
import model.formulas.Disjunction;
import model.formulas.Formula;
import model.formulas.Negation;
import start.Constants;

public class LawOfExcludedMiddle extends Rule {

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

	private boolean verifySecondArgNeg(Formula lhs, Formula rhs) {
		if(!(rhs instanceof Negation)) {
			return false;
		}
		Negation neg = (Negation) rhs;
		Formula notNeg = neg.formula;
		if(!(notNeg.equals(lhs))) {
			return false;
		}
		return true;
	}

	@Override
	public boolean verifyRow(Box data, int rowIndex) {
		Formula rowToVerify = data.getRow(rowIndex).getFormula();
		if (!(rowToVerify instanceof Disjunction)) {
            return false;
        }
		Disjunction disj = (Disjunction) rowToVerify;
		Formula lhs = disj.lhs; 
		Formula rhs = disj.rhs;
		return (verifySecondArgNeg(lhs,rhs) || verifySecondArgNeg(rhs,lhs));
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
		return "LEM";
	}
	
	@Override
    public String toString() {
        return Constants.lawOfExcludedMiddle;
	}

}
