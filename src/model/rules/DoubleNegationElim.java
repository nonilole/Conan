package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.Negation;

public class DoubleNegationElim implements Rule {
    private Integer premise1;
    public DoubleNegationElim() {}
    public DoubleNegationElim(int premise1Index) {
        this.premise1 = premise1Index;
    }
    public Integer getPremise1() {
        return premise1;
    }

    public void setPremise1(int premise1) {
        this.premise1 = premise1;
    }
    @Override
    public boolean hasCompleteInfo() {
        return premise1 != null;
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        Formula premise = data.getRow(getPremise1()).getFormula();
        if (data.isInScopeOf(getPremise1(), rowIndex) == false) return false;
        if (premise instanceof Negation == false) {
            return false;
        } else {
            Negation negation = (Negation) premise;
            premise = negation.formula;
            if (premise instanceof Negation == false) {
                return false;
            } else {
                negation = (Negation) premise;
                return negation.formula.equals(data.getRow(rowIndex).getFormula());
            }
        }
    }

    @Override
    public String toString(){
        String p1 = premise1 == null ? "" : premise1.toString();
        return String.format("¬¬-E (%s)", p1);
    }
}
