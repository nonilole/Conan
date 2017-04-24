package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import model.formulas.QuantifiedFormula;

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
        if (!(refBox.getRow(0).getFormula() instanceof FreshVarFormula)) return false;
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
        return toVerify.instantiate(freshVarId).equals(lastRowInRefBox);
    }

    @Override
    public Formula generateRow(Box data) {
        Box refBox = data.getBox(intervalRef);
        String freshVarId = ((FreshVarFormula) refBox.getRow(0).getFormula()).var;
        Formula lastRowInRefBox = refBox.getRow(refBox.size() - 1).getFormula();
        return new QuantifiedFormula(lastRowInRefBox, freshVarId, '∀'); // Maybe wrong string?
    }

    @Override
    public String toString() {
        return "∀I (" + intervalRef + ")";
    }

}
