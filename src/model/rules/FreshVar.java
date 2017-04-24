package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;

public class FreshVar extends Rule {

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public void updateReference(int index, String newValue) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        ProofRow row = data.getRow(rowIndex);
        if (row.getParent().getRow(0) != row) return false;
        if (row.getFormula() instanceof FreshVarFormula == false) return false;
        //TODO: check that the var is actually fresh!
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        ProofRow row = data.getRow(rowIndex);
        if (row.getParent().getRow(0) != row) return false;
        if (row.getFormula() instanceof FreshVarFormula == false) return false;
        //TODO: check that the var is actually fresh!
        return true;
    }

    @Override
    public String toString() {
        return "Fresh var.";
    }

}
