package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.Negation;
import start.Constants;

public class DoubleNegationElim extends Rule {
    private Integer premise1;

    public DoubleNegationElim() {
    }

    public DoubleNegationElim(int premise1Index) {
        this.premise1 = premise1Index;
    }

    public Integer getPremise1() {
        return premise1;
    }

    public void setPremise1(Integer premise1) {
        this.premise1 = premise1;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        if (refNr != 1) throw new IllegalArgumentException();
        Integer ref;
        try {
            ref = ReferenceParser.parseIntegerReference(refStr);
            setPremise1(ref);
        } catch (NumberFormatException e) {
            ref = null;
            setPremise1(ref);
            throw new NumberFormatException(); //Still want this to propagate up
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (data.isInScopeOf(getPremise1(), rowIndex) == false) return false;
        Formula premise = data.getRow(getPremise1()).getFormula();
        if (premise instanceof Negation == false) {
            return false;
        }
        Negation negation = (Negation) premise;
        premise = negation.formula;
        if (premise instanceof Negation == false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula premise = data.getRow(getPremise1()).getFormula();
        Negation negation = (Negation) premise;
        negation = (Negation) negation.formula;
        return negation.formula.equals(data.getRow(rowIndex).getFormula());
    }

    @Override
    public Formula generateRow(Box data) {
        Formula premise = data.getRow(getPremise1()).getFormula();
        Negation negation = (Negation) premise;
        negation = (Negation) negation.formula;
        return negation.formula;
    }

    @Override
    public boolean hasCompleteInfo() {
        return premise1 != null;
    }

    @Override
    public String toString() {
        String p1 = premise1 == null ? "" : new Integer(premise1+1).toString();
        return String.format("¬¬e, %s", p1);
    }
    
    @Override
	public String[] getReferenceStrings() {
    	String ref1 = premise1 == null ? "" : (premise1+1)+"";
		return new String[]{ref1};
	}
    
	@Override
	public String getDisplayName() {
        return Constants.doubleNegationElim;
	}
}
