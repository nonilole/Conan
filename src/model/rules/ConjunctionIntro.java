package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Conjunction;
import model.formulas.Formula;
import start.Constants;

public class ConjunctionIntro extends Rule {

    //indexes of the premises
    private Integer premise1;
    private Integer premise2;

    public ConjunctionIntro() {
        super();
    }

    public ConjunctionIntro(int premise1Index, int premise2Index) {
        super();
        premise1 = premise1Index;
        premise2 = premise2Index;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        if (refNr < 1 || refNr > 2) throw new IllegalArgumentException();
        Integer ref;
        try {
            ref = ReferenceParser.parseIntegerReference(refStr);
            if (refNr == 1) setPremise1(ref);
            else/*refNr == 2*/ setPremise2(ref);
        } catch (NumberFormatException e) {
            ref = null;
            if (refNr == 1) setPremise1(ref);
            else/*refNr == 2*/ setPremise2(ref);
            throw new NumberFormatException(); //Still want this to propagate up
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (data.isInScopeOf(getPremise1(), rowIndex) == false ||
                data.isInScopeOf(getPremise2(), rowIndex) == false) {
            System.out.println("    Scope issue");
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        ProofRow rowToVerify = data.getRow(rowIndex);
        if (rowToVerify.getFormula() instanceof Conjunction == false) return false;
        Conjunction conclusion = (Conjunction) rowToVerify.getFormula();
        Formula content1 = data.getRow(getPremise1()).getFormula();
        Formula content2 = data.getRow(getPremise2()).getFormula();
        return conclusion.equals(new Conjunction(content1, content2));
    }

    @Override
    public Formula generateRow(Box data) {
        Formula content1 = data.getRow(getPremise1()).getFormula();//this currently assumes the premise is stored as a rowNr rather than index
        Formula content2 = data.getRow(getPremise2()).getFormula();
        return new Conjunction(content1, content2);
    }

    @Override
    public boolean hasCompleteInfo() {
        return premise1 != null && premise2 != null;
    }

    public Integer getPremise1() {
        return premise1;
    }

    public void setPremise1(Integer premise1) {
        this.premise1 = premise1;
    }

    public Integer getPremise2() {
        return premise2;
    }

    public void setPremise2(Integer premise2) {
        this.premise2 = premise2;
    }
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = premise1 == null ? "" : (premise1+1)+"";
		String ref2 = premise2 == null ? "" : (premise2+1)+"";
		return new String[]{ref1, ref2};
	}

	@Override
	public String getDisplayName() {
		return Constants.conjunctionIntro;
	}

    @Override
    public String toString() {
        String p1 = premise1 == null ? "" : new Integer(premise1+1).toString();
        String p2 = premise2 == null ? "" : new Integer(premise2+1).toString();
        return String.format("∧i, %s,%s", p1, p2);
    }
}
