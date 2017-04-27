package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import model.formulas.Formula;
import model.rules.Rule;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow implements ProofEntry{
    private Formula formula;
    private String userInput = "";
    private Rule rule;
    private Box parent;
    private boolean isWellFormed = true;
    private boolean isVerified = false;;
    
    public ProofRow(Box parent){
    	this.parent = parent;
    }
    
    public Box getParent(){
    	return parent;
    }
    public void setParent(Box parent) {
        this.parent = parent;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public boolean isWellFormed() {
        return this.isWellFormed;
    }
    public boolean isVerified() {
        return this.isVerified;
    }

    public void setWellformed(boolean b) {
        this.isWellFormed = b;
    }
    public void setVerified(boolean b) {
        this.isVerified = b;
    }
    public Formula getFormula() {
        return this.formula;

    }
    public Rule getRule() {
        return this.rule;
    }
    
    @Override 
    public String toString(){
    	StringBuilder strB = new StringBuilder();
    	if(formula == null){
    		strB.append(getUserInput());
    	}
    	else{
    		strB.append(formula.toString());
    	}
    	strB.append(" :: "+(rule == null ? "no rule" : rule)); // ✓
    	strB.append(" :: Verified: " + (isVerified ? "✓" : "x"));
        strB.append(" ::" + (!getParent().isTopLevelBox() && getParent().entries.get(0).equals(this) ? "1" : "0")); //For latex to indicate start of a box
    	return strB+"";
    }

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
}
