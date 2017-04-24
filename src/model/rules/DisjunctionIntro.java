package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Disjunction;
import model.formulas.Formula;

public class DisjunctionIntro implements Rule {

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
    public void updateReference(int refNr, String refStr){
      if(refNr != 1) throw new IllegalArgumentException();
      Integer ref;
      try{
        ref = ReferenceParser.parseIntegerReference(refStr);
      }
      catch(NumberFormatException e){
        ref = null;
        throw new NumberFormatException(); //Still want this to propagate up
      }
      setPremise(ref);
    }
	
	@Override
	public boolean hasCompleteInfo() {
		return premise != null;
	}

	public Integer getPremise() {
		return premise;
	}

	public void setPremise(int premise) {
		this.premise = premise;
	}

	@Override
	public String toString(){
		String p1 = premise == null ? "" : premise.toString();
		return String.format("∧-I (%s)",p1);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		ProofRow rowToVerify = data.getRow(rowIndex);

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check scope and if the data is verified
		if (!data.isInScopeOf(getPremise(), rowIndex)) {
			return false;
		}

		// do we have the needed premises/references to make the deduction?
        if (!(rowToVerify.getFormula() instanceof Disjunction)) { return false;
        }

        Disjunction disjunction = (Disjunction) rowToVerify.getFormula();
        ProofRow premiseRow = data.getRow(getPremise());
        if (type == 1)
            return disjunction.lhs.equals(premiseRow.getFormula());
        return disjunction.rhs.equals(premiseRow.getFormula());
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
		return new String[]{this.premise+""};
	}
	@Override
	public String getDisplayName() {
		// implemented in subclasses
		return "∨I";
	}

}
