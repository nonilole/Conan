package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import model.formulas.QuantifiedFormula;
import start.Constants;

public class ExistsElim extends Rule {
    private Integer rowRef;
    private Interval intervalRef;
    private String var = null;

    public ExistsElim() {
    }

    public ExistsElim(char var) {
        this.var = Character.toString(var);
    }

    @Override
    public boolean hasCompleteInfo() {
        return intervalRef != null && rowRef != null;
    }

    @Override
    public void updateReference(int index, String newValue) {
        if (index < 1 || index > 2) throw new IllegalArgumentException();

        if (index == 1) {
            try {
                rowRef = ReferenceParser.parseIntegerReference(newValue);
            } catch (NumberFormatException e) {
                rowRef = null;
                throw new NumberFormatException();
            }
        } else {//index == 2
            try {
                intervalRef = ReferenceParser.parseIntervalReference(newValue);
            } catch (NumberFormatException e) {
                intervalRef = null;
                throw new NumberFormatException();
            }
        }

    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!data.isInScopeOf(rowRef, rowIndex)) return false;
        if (!data.isInScopeOf(intervalRef, rowIndex)) return false;
        ProofRow referencedRow = data.getRow(rowRef);
        Formula referencedRowFormula = referencedRow.getFormula();
        if (referencedRowFormula instanceof QuantifiedFormula) {
            QuantifiedFormula quant = (QuantifiedFormula) referencedRowFormula;
            if (quant.type != '∃') return false;
            if (var != null && !quant.var.equals(var)) return false;
        } else return false;
        QuantifiedFormula refdQuant = (QuantifiedFormula) referencedRowFormula;
        Box refBox = data.getBox(intervalRef);
        if (refBox.size() < 3)
            return false;//needs at least fresh var, instantiation of quantifiedFormula and conclusion not containing fresh var
        if (!(refBox.getRow(0).getRule() instanceof FreshVar)) return false;
        String freshVar = ((FreshVarFormula) refBox.getRow(0).getFormula()).var;
        if (!refBox.getRow(1).getFormula().equals(refdQuant.instantiate(freshVar))) return false;
        if (refBox.getRow(refBox.size() - 1).getFormula().containsFreeObjectId(freshVar)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Box refBox = data.getBox(intervalRef);
        if (data.getRow(rowIndex).getFormula().equals(refBox.getRow(refBox.size() - 1).getFormula()) == false)
            return false;
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        Box refBox = data.getBox(intervalRef);
        return refBox.getRow(refBox.size() - 1).getFormula();
    }

    @Override
    public String toString() {
        return String.format("∃e, %s, %s", rowRef == null ? "" : new Integer(rowRef+1), intervalRef);
    }

	@Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef == null ? "" : (rowRef+1)+"";
		String ref2 = intervalRef == null ? "" : (intervalRef.startIndex+1)+"-"+(intervalRef.endIndex+1);
		return new String[]{ref1, ref2};
	}

	@Override
	public String getDisplayName() {
        return Constants.existsElim;
	}
}
