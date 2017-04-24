package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;

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
        } catch (NumberFormatException e) {
            ref = null;
            throw new NumberFormatException(); //Still want this to propagate up
        }
        setPremise(ref);
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
}
