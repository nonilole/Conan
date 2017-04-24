package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Equality;
import model.formulas.Formula;

public class EqualityIntro implements Rule {

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }
    
    @Override
    public void updateReference(int refNr, String refStr){
      throw new IllegalArgumentException();
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        Formula result = data.getRow(rowIndex).getFormula();
        if (result instanceof Equality == false) {
            return false;
        } else {
            Equality equality = (Equality) result;
            return equality.lhs.equals(equality.rhs);
        }
    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        return null;
    }

    @Override
    public String toString() {
        return "=-I";
    }
    
    @Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
		return "=I";
	}
}
