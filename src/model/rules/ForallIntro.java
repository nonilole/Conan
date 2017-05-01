package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import model.formulas.QuantifiedFormula;
import start.Constants;


public class ForallIntro extends Rule {
    private Interval intervalRef;

    @Override
    public boolean hasCompleteInfo() {
        return intervalRef != null;
    }

    @Override
    public void updateReference(int index, String newValue) {
        if (index != 1) throw new IllegalArgumentException();
        try {
            intervalRef = ReferenceParser.parseIntervalReference(newValue);
        } catch (NumberFormatException e) {
            intervalRef = null;
            throw new NumberFormatException();
        }

    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!(data.isInScopeOf(intervalRef, rowIndex))) return false;
        Box refBox = data.getBox(intervalRef);
        if (refBox.size() < 2) return false;
        if (!(refBox.getRow(0).getRule() instanceof FreshVar)) return false;
        
        //check that the second row in box isn't an assumption
        if(refBox.size() < 2 ) return false;
        //if ((refBox.getRow(1).getRule() instanceof Assumption)) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Box refBox = data.getBox(intervalRef);
        String freshVarId = ((FreshVarFormula) refBox.getRow(0).getFormula()).var;
        Formula lastRowInRefBox = refBox.getRow(refBox.size() - 1).getFormula();
        QuantifiedFormula toVerify;
        if (data.getRow(rowIndex).getFormula() instanceof QuantifiedFormula == false) return false;
        toVerify = (QuantifiedFormula) data.getRow(rowIndex).getFormula();
        if(toVerify.type != '∀') return false;
        return toVerify.instantiate(freshVarId).equals(lastRowInRefBox);
    }

    @Override
    public Formula generateRow(Box data) {
//        Box refBox = data.getBox(intervalRef);
//        String freshVarId = ((FreshVarFormula) refBox.getRow(0).getFormula()).var;
//        Formula lastRowInRefBox = refBox.getRow(refBox.size() - 1).getFormula();
//        // We can't know which variable to generate
//        return new QuantifiedFormula(lastRowInRefBox, freshVarId, '∀');
        return null;
    }

    @Override
    public String toString() {
        return String.format("∀i, %s", intervalRef);
    }
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = intervalRef == null ? "" : (intervalRef.startIndex+1)+"_"+(intervalRef.endIndex+1);
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
        return Constants.forallIntro;
	}

}
