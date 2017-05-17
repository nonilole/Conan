package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;
import start.Constants;

public class ContradictionElim extends Rule {

    private Integer premise;

    public ContradictionElim() {};
    public ContradictionElim(Integer premise) {
        this.premise = premise;
    }

    @Override
    public boolean hasCompleteInfo() {
        return premise != null;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        if (refNr != 1) throw new IllegalArgumentException();
        Integer ref;
        try {
            ref = ReferenceParser.parseIntegerReference(refStr);
            setPremise(ref);
        } catch (NumberFormatException e) {
            ref = null;
            setPremise(ref);
            throw new NumberFormatException(); //Still want this to propagate up
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!data.isInScopeOf(getPremise(), rowIndex)) return false;
        Formula premise = data.getRow(getPremise()).getFormula();
        if (premise instanceof Contradiction) return true;
        return false;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }


    @Override
    public String toString() {
        return String.format("⊥e, %s", premise == null ? "" : new Integer(premise+1));
    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        return null;
    }

    public Integer getPremise() {
        return premise;
    }

    public void setPremise(Integer premise) {
        this.premise = premise;
    }
    
	@Override
	public String[] getReferenceStrings() {
		return new String[]{ premise == null ? "" : (premise+1)+""};
	}
	@Override
	public String getDisplayName() {
		return Constants.contradictionElim;
	}
    
    
}
