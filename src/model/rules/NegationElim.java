package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;
import model.formulas.Negation;
import start.Constants;

public class NegationElim extends Rule {
    private Integer rowRef1;
    private Integer rowRef2;

    public NegationElim() {
        super();
    }

    public NegationElim(Integer rowRef1, Integer rowRef2) {
        super();
        this.rowRef1 = rowRef1;
        this.rowRef2 = rowRef2;
    }

    @Override
    public boolean hasCompleteInfo() {
        return rowRef1 != null && rowRef2 != null;
    }

    /**
     * Updates the values for the three rule prompts.
     *
     * @param index:    the index for the rule prompt box.
     * @param newValue: the new value for that specific rule prompt box.
     */
    @Override
    public void updateReference(int index, String newValue) {
        if (index < 1 || index > 2) throw new IllegalArgumentException();

        if (index == 1) {
            try {
                rowRef1 = ReferenceParser.parseIntegerReference(newValue);
            } catch (NumberFormatException e) {
                rowRef1 = null;
                throw new NumberFormatException();
            }
        } else {//index == 2
            try {
                rowRef2 = ReferenceParser.parseIntegerReference(newValue);
            } catch (NumberFormatException e) {
                rowRef2 = null;
                throw new NumberFormatException();
            }
        }
    }

    private boolean secondArgNegation(Formula rowRef1Formula, Formula rowRef2Formula) {
        if (!(rowRef2Formula instanceof Negation)) {
            return false;
        }
        Negation neg = (Negation) rowRef2Formula;
        Formula rowRef2NoNeg = neg.formula;
        if (!rowRef2NoNeg.equals(rowRef1Formula)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (data.isInScopeOf(rowRef1, rowIndex) == false) return false;
        if (data.isInScopeOf(rowRef2, rowIndex) == false) return false;
        Formula rowRef1Formula = data.getRow(rowRef1).getFormula();
        Formula rowRef2Formula = data.getRow(rowRef2).getFormula();
        return secondArgNegation(rowRef1Formula, rowRef2Formula) ||
                secondArgNegation(rowRef2Formula, rowRef1Formula);
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        Formula rowToVerifyFormula = data.getRow(rowIndex).getFormula();
        return (rowToVerifyFormula instanceof Contradiction);
    }

    @Override
    public Formula generateRow(Box data) {
        return new Contradiction();
    }


    @Override
    public String toString() {
        String p1 = rowRef1 == null ? "" : new Integer(rowRef1+1).toString();
        String p2 = rowRef2 == null ? "" : new Integer(rowRef2+1).toString();
        return String.format("¬e, %s, %s", p1, p2);
    }
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef1 == null ? "" : (rowRef1+1)+"";
		String ref2 = rowRef2 == null ? "" : (rowRef2+1)+"";
		return new String[]{ref1, ref2};
	}

	@Override
	public String getDisplayName() {
        return Constants.negationElim;
	}
}
