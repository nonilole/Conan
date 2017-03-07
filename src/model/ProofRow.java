package model;
import model.formulas.Formula;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow {
    private Formula formula;
    private String rule;
    private int depth;
    private boolean formulaIsWellFormed;
    private boolean ruleIsWellFormed;
    private boolean isValid;
    private boolean matchesConclusion;

    public ProofRow(Formula formula, String rule, int depth) {
        this.formula = formula;
        this.rule = rule;
        this.depth = depth;
        this.formulaIsWellFormed = true;
        this.ruleIsWellFormed = true;
        this.isValid = true;
        this.matchesConclusion = false;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public boolean isFormulaWellFormed() {
        return this.formulaIsWellFormed;
    }
    public boolean isRuleWellFormed() {
        return this.ruleIsWellFormed;
    }
    public boolean isValid() {
        return this.isValid;
    }
    public boolean matchesConclusion() {
        return this.matchesConclusion;
    }

    public void setFormulaWellformed(boolean b) {
        this.formulaIsWellFormed = b;
    }
    public void setRuleWellFormed(boolean b) {
        this.ruleIsWellFormed = b;
    }
    public void setValid(boolean b) {
        this.isValid = b;
    }
    public void setMatchesConclusion(boolean b) {
        this.matchesConclusion = b;
    }

    public Formula getFormula() {
        return this.formula;

    }
    public String getRule() {
        return this.rule;
    }
    public int getDepth() { return this.depth; }
}
