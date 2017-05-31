package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;
import start.Constants;

public class ExistsIntro extends Rule {
    private Integer rowRef;
    private String var = null;

    public ExistsIntro() {}
    public ExistsIntro(char var) {
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
        if (!data.isInScopeOf(this.rowRef, rowIndex)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula toVerify = data.getRow(rowIndex).getFormula();
        Formula ref = data.getRow(this.rowRef).getFormula();
        if (toVerify instanceof QuantifiedFormula) {
        	QuantifiedFormula quantToVerify = (QuantifiedFormula) toVerify;
        	if (quantToVerify.type != '∃') return false;
            if (var != null && !quantToVerify.var.equals(var))
                return false;
            return Formula.isInstantiationOf(ref, quantToVerify);
        }
        return false;
    }

    @Override
    public Formula generateRow(Box data) {
        if (var == null)
            return null;
        return new QuantifiedFormula(data.getRow(this.rowRef).getFormula(), var, '∃');
    }

    @Override
    public String toString() {
        return String.format("∃i, %s", rowRef == null ? "" : new Integer(rowRef+1));
    }
    
    @Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef == null ? "" : (rowRef+1)+"";
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
        if (var != null)
            return Constants.exists + var + Constants.introduction;
        return Constants.existsIntro;
	}
}
