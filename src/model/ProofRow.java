package model;
import model.formulas.Formula;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow {
    private Formula formula;
    private String rule;
    private int depth;

    public ProofRow(Formula formula, String rule, int depth) {
        this.formula = formula;
        this.rule = rule;
        this.depth = depth;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Formula getFormula() {
        return this.formula;

    }
    public String getRule() {
        return this.rule;
    }
    public int getDepth() { return this.depth; }
}
