package model;
import model.formulas.Formula;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow {
    private Formula formula;
    private String rule;

    public ProofRow(Formula formula, String rule) {
        this.formula = formula;
        this.rule = rule;
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
}
