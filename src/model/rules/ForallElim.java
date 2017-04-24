package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;

public class ForallElim extends Rule {
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
        if (!(data.isInScopeOf(rowRef, rowIndex)))
            return false;
        if (data.getRow(rowRef).getFormula() instanceof QuantifiedFormula == false) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        return false;
    }

    @Override
    public Formula generateRow(Box data) {
        QuantifiedFormula ref = (QuantifiedFormula) data.getRow(rowRef).getFormula();
        return ref.formula;
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        QuantifiedFormula ref = (QuantifiedFormula) data.getRow(rowRef).getFormula();
        Formula toVerify = data.getRow(rowIndex).getFormula();
        return Formula.isInstantiationOf(toVerify, ref);

    }

    @Override
    public String toString() {
        return "∀E (" + rowRef + ")";
    }
}
