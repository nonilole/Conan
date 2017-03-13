package model;
import java.util.List;

import model.formulas.Formula;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow {
	private List<Box> boxes;
    private Formula formula;
    private String rule = "";
    private boolean isWellFormed = true;
    //private boolean ruleIsWellFormed; //user should ot type in rules, no need to track if it's wellformed
    private boolean isVerified = false;;
    //private boolean matchesConclusion; // th row doesn't need to know this

    public ProofRow(List<Box> boxes){
    	this.boxes = boxes;
    }
    

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public void setRule(String rule) {
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
    public String getRule() {
        return this.rule;
    }
}
