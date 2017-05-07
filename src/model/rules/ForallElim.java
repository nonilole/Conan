package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;
import start.Constants;


public class ForallElim extends Rule {
    private Integer rowRef;
    private String var = null;

    public ForallElim() {}
    public ForallElim(char var) {
        this.var = Character.toString(var);
    }

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
        if (((QuantifiedFormula) data.getRow(rowRef).getFormula()).type != '∀') return false;
        // TODO: Check if any of the quantifiers match our variable and type.
        if (var != null && !((QuantifiedFormula) data.getRow(rowRef).getFormula()).var.equals(var)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
    	QuantifiedFormula ref = (QuantifiedFormula) data.getRow(rowRef).getFormula();
        Formula toVerify = data.getRow(rowIndex).getFormula();
        return Formula.isInstantiationOf(toVerify, ref);
    }

    @Override
    public Formula generateRow(Box data) {
        QuantifiedFormula ref = (QuantifiedFormula) data.getRow(rowRef).getFormula();
        return ref.instantiate(ref.var + "₀");
    }

    @Override
    public String toString() {
        return String.format("∀e, %s", rowRef == null ? "" : new Integer(rowRef+1));
    }
    
    @Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef == null ? "" : (rowRef+1)+"";
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
        if (var != null)
            return Constants.forall + var + Constants.elimination;
        return Constants.forallElim;
	}
}
