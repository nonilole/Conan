package model.rules;

import model.Box;
import model.formulas.Equality;
import model.formulas.Formula;

import java.util.ArrayList;

public class EqualityElim extends Rule {
    private Integer rowRef1;
    private Integer rowRef2;

    @Override
    public boolean hasCompleteInfo() {
        return rowRef1 != null && rowRef2 != null;
    }

    @Override
    public void updateReference(int index, String newValue) {
        if (index < 1 || index > 2) throw new IllegalArgumentException();

        if (index == 1) {
            try {
                rowRef1 = ReferenceParser.parseIntegerReference(newValue);
            } catch (NumberFormatException e) {
                rowRef1 = null;
                throw new NumberFormatException();
            }
        } else {//index == 2
            try {
                rowRef2 = ReferenceParser.parseIntegerReference(newValue);
            } catch (NumberFormatException e) {
                rowRef2 = null;
                throw new NumberFormatException();
            }
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!data.isInScopeOf(rowRef1, rowIndex)) return false;
        if (!data.isInScopeOf(rowRef2, rowIndex)) return false;

        if (!(data.getRow(rowRef1).getFormula() instanceof Equality)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Equality eq = (Equality) data.getRow(rowRef1).getFormula();
        Formula ref = data.getRow(rowRef2).getFormula();
        Formula toVerify = data.getRow(rowIndex).getFormula();
        return Formula.almostEqual(ref, toVerify, eq, new ArrayList<String>());
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public String toString() {
        return "=E " + rowRef1 + ", " + rowRef2;
    }

}
