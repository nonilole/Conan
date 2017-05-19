package model.rules;

import model.Box;
import model.ProofRow;
import model.VerificationInputException;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import start.Constants;

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
        if(row.getParent().isTopLevelBox()) throw new VerificationInputException("Fresh needs to be in a box.");

        if (row.getParent().getRow(0) != row) throw new VerificationInputException("Fresh needs to be first in a box.");
        //check that this is inside a box
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
        		prowAncestorBox = prowAncestorBox.getParent();
        	}
        	if(isInSameBox) continue; //if the formula is in the same box as the fresh var it's allowed to contain the var
        	else{
        		Formula f = prow.getFormula();
        		if(f == null) continue;
        		if(f.containsFreeObjectId(freshVar)) throw new VerificationInputException("Variable is used elsewhere.");
        	}
        }
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public String toString() {
        return "fresh";
    }

	@Override
	public String[] getReferenceStrings() {
		return new String[0];
	}

	@Override
	public String getDisplayName() {
        return Constants.freshVar;
	}

}
