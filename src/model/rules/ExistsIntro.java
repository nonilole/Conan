package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;

public class ExistsIntro extends Rule {
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
        if (!data.isInScopeOf(this.rowRef, rowIndex)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula toVerify = data.getRow(rowIndex).getFormula();
        Formula ref = data.getRow(this.rowRef).getFormula();
        if (toVerify instanceof QuantifiedFormula) {
            return Formula.isInstantiationOf(ref, (QuantifiedFormula) toVerify);
        }
        return false;
    }

    @Override
    public Formula generateRow(Box data) {
        Formula ref = data.getRow(this.rowRef).getFormula();
        return new QuantifiedFormula(ref, "x", '∃'); // Maybe not x?
    }

    @Override
    public String toString() {
        return String.format("∃e, %s", rowRef == null ? "" : new Integer(rowRef+1));
    }
    
    @Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef == null ? "" : (rowRef+1)+"";
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
		return "∃I";
	}
}
