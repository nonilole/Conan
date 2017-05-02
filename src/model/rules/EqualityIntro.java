package model.rules;

import model.Box;
import model.formulas.Equality;
import model.formulas.Formula;
import start.Constants;

public class EqualityIntro extends Rule {

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula result = data.getRow(rowIndex).getFormula();
        if (result instanceof Equality == false) {
            return false;
        } else {
            Equality equality = (Equality) result;
            return equality.lhs.equals(equality.rhs);
        }
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public String toString() {
        return "=i";
    }
    
    @Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
        return Constants.equalityIntro;
	}
}
