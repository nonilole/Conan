package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Equality;
import model.formulas.Formula;

public class EqualityIntro implements Rule {

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        Formula formula = data.getRow(rowIndex).getFormula();
        if (formula instanceof Equality == false) {
            return false;
        } else {
            Equality equality = (Equality) formula;
            return equality.lhs == equality.rhs;
        }
    }
}
