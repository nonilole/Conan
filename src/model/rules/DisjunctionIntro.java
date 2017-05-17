package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Disjunction;
import model.formulas.Formula;
import start.Constants;

public class DisjunctionIntro extends Rule {

    private Integer premise;
    private int type;

    public DisjunctionIntro(int type) {
        super();
        if (type != 1 && type != 2)
            throw new IllegalArgumentException("DisjunctionElim(int type): type must be 1 or 2");
        this.type = type;
    }

    public DisjunctionIntro(int type, int premise) {
        super();
        if (type != 1 && type != 2)
            throw new IllegalArgumentException("DisjunctionElim(int type): type must be 1 or 2");
        this.type = type;
        this.premise = premise;
    }

    @Override
    public void updateReference(int refNr, String refStr) {
        if (refNr != 1) throw new IllegalArgumentException();
        Integer ref;
        try {
            ref = ReferenceParser.parseIntegerReference(refStr);
            setPremise(ref);
        } catch (NumberFormatException e) {
            ref = null;
            setPremise(ref);
            throw new NumberFormatException(); //Still want this to propagate up
        }
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        if (!data.isInScopeOf(getPremise(), rowIndex)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        ProofRow rowToVerify = data.getRow(rowIndex);
        if (!(rowToVerify.getFormula() instanceof Disjunction)) {
            return false;
        }
        Disjunction disjunction = (Disjunction) rowToVerify.getFormula();
        ProofRow premiseRow = data.getRow(getPremise());
        if (type == 1)
            return disjunction.lhs.equals(premiseRow.getFormula());
        return disjunction.rhs.equals(premiseRow.getFormula());
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }
	@Override
	public Formula generateFormula(Box data, int rowIndex) {
	    return null;
//		if (!data.isInScopeOf(getPremise(), rowIndex)) return null;
//		Formula premise = data.getRow(getPremise()).getFormula();
//		if (type == 1)
//            return new Disjunction(premise, null);// PLACEHOLDER
//		return new Disjunction(null, premise); // WANT TO GENERATE EMPTY FORMULA
//		return new Disjunction(premise, null);
//		return new Disjunction(null, premise);
	}
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = premise == null ? "" : (premise+1)+"";
		return new String[]{ref1};
	}
	@Override
	public String getDisplayName() {
        if (type == 1)
            return Constants.disjunctionIntro1;
        else
            return Constants.disjunctionIntro2;
	}

    @Override
    public boolean hasCompleteInfo() {
        return premise != null;
    }

    public Integer getPremise() {
        return premise;
    }

    public void setPremise(Integer premise) {
        this.premise = premise;
    }

    @Override
    public String toString() {
        String p1 = premise == null ? "" : new Integer(premise+1).toString();
        return String.format("∧i_{%s}, %s", type, p1);
    }
}
