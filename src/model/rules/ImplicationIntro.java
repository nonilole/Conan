package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.Implication;
import start.Constants;

public class ImplicationIntro extends Rule {

    private Interval premiseInterval;

    public ImplicationIntro() {
        super();
    }

    public ImplicationIntro(Interval indexInterval) {
        super();
        premiseInterval = indexInterval;
    }

    public Interval getPremiseInterval() {
        return premiseInterval;
    }

    public void setPremiseInterval(Interval premiseInterval) {
        this.premiseInterval = premiseInterval;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        if (refNr != 1) throw new IllegalArgumentException();
        Interval ref;
        try {
            ref = ReferenceParser.parseIntervalReference(refStr);
            setPremiseInterval(ref);
        } catch (NumberFormatException e) {
            ref = null;
            setPremiseInterval(ref);
            throw new NumberFormatException(); //Still want this to propagate up
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        Interval premiseInterval = getPremiseInterval();
        if (!(data.isInScopeOf(premiseInterval, rowIndex))) return false;
        Rule rule = data.getRow(premiseInterval.startIndex).getRule();
        if (rule instanceof Assumption == false) return false;
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        ProofRow rowToVerify = data.getRow(rowIndex);
        if (rowToVerify.getFormula() instanceof Implication == false) return false;
        Implication conclusion = (Implication) rowToVerify.getFormula();
        Formula assumption = data.getRow(premiseInterval.startIndex).getFormula();
        Formula conclusionOfBox = data.getRow(premiseInterval.endIndex).getFormula();
        return conclusion.equals(new Implication(assumption, conclusionOfBox));
    }

    @Override
    public Formula generateRow(Box data) {
        Formula assumption = data.getRow(premiseInterval.startIndex).getFormula();
        Formula conclusionOfBox = data.getRow(premiseInterval.endIndex).getFormula();
        return new Implication(assumption, conclusionOfBox);
    }

    @Override
    public boolean hasCompleteInfo() {
        return premiseInterval != null;
    }

    @Override
    public String toString() {
        return String.format("→i, %s", premiseInterval);
    }
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = premiseInterval == null ? "" : (premiseInterval.startIndex+1)+"-"+(premiseInterval.endIndex+1);
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
        return Constants.implicationIntro;
	}
}
