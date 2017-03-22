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
	//private List<Box> boxes; //First box in list should be the top-level box, last box should be the innermost one
    private Formula formula;
    private String userInput = "";
    private Rule rule;
    private Box parent;
    private boolean isWellFormed = true;
    //private boolean ruleIsWellFormed; //user should ot type in rules, no need to track if it's wellformed?
    private boolean isVerified = false;;
    //private boolean matchesConclusion; // th row doesn't need to know this
    
    public ProofRow(Box parent){
    	this.parent = parent;
    }
    
    public Box getParent(){
    	return parent;
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
    
    @Override //TODO: also represent the rule...
    public String toString(){
    	if(formula == null){
    		return getUserInput();
    	}
    	else{
    		return formula.toString();
    	}
    }

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
}
