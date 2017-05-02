package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;
import model.formulas.Negation;
import start.Constants;

public class NegationIntro extends Rule {

    private Interval interval;

    public NegationIntro() {
        super();
    }

    public NegationIntro(Interval interval) {
        super();
        this.interval = interval;
    }

    @Override
    public boolean hasCompleteInfo() {
        return interval != null;
    }

    @Override
    public void updateReference(int index, String newValue) {
        if (index < 1 || index > 1) throw new IllegalArgumentException();

        if (index == 1) {
            try {
                interval = ReferenceParser.parseIntervalReference(newValue);
            } catch (NumberFormatException e) {
                interval = null;
                throw new NumberFormatException();
            }
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!(data.isInScopeOf(interval, rowIndex))) return false;
        Formula intervalEndFormula = data.getRow(interval.endIndex).getFormula();
        return (intervalEndFormula instanceof Contradiction);
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();
        Formula RowToVerifyFormula = data.getRow(rowIndex).getFormula();
        if (!(RowToVerifyFormula instanceof Negation)) {
            return false;
        }
        Negation neg = (Negation) RowToVerifyFormula;
        Formula RowToVerifyFormulaNoNeg = neg.formula;
        return intervalStartFormula.equals(RowToVerifyFormulaNoNeg);
    }

    @Override
    public Formula generateRow(Box data) {
        Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();
        return new Negation(intervalStartFormula);
    }

    @Override
    public String toString() {
        return String.format("¬i, %s", interval);
    }
    @Override
	public String[] getReferenceStrings() {
		String ref1 = interval == null ? "" : (interval.startIndex+1)+"-"+(interval.endIndex+1);
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
		return Constants.negationIntro;
	}
}
