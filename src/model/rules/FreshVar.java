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
        //check that this rule is applied on the first row of the box
        if (row.getParent().getRow(0) != row) return false;
        //check that this is inside a box
        if(row.getParent().isTopLevelBox()) return false;
        //check that the formula is of the correct type
        if (row.getFormula() instanceof FreshVarFormula == false) return false;
        //check that the variable is actually fresh
        String freshVar = ((FreshVarFormula)row.getFormula()).var;
        for(int i = 0; i < data.size(); i++){
        	
        	ProofRow prow = data.getRow(i);
        	Box prowAncestorBox = prow.getParent();
        	boolean isInSameBox = false;
        	
        	while(prowAncestorBox.isTopLevelBox() == false){
        		//have to update prowAncestor box... duh
        		if( prowAncestorBox == row.getParent()){
        			isInSameBox = true;
        			break;
        		}
        	}
        	if(isInSameBox) continue; //if the formula is in the same box as the fresh var it's allowed to contain the var
        	else{
        		Formula f = prow.getFormula();
        		if(f == null) continue;
        		if(f.containsObjectId(freshVar)) return false;
        	}
        }
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    /*@Override
    public boolean verify(Box data, int rowIndex) {
        ProofRow row = data.getRow(rowIndex);
        if (row.getParent().getRow(0) != row) return false;
        if (row.getFormula() instanceof FreshVarFormula == false) return false;
        //TODO: check that the var is actually fresh!
        return true;
    }*/

    @Override
    public String toString() {
        return "Fresh var.";
    }

	@Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
		return "Fresh";
	}

}
