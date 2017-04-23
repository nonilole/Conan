package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Conjunction;
import model.formulas.Formula;

public class ConjunctionIntro implements Rule{

	//indexes of the premises
	private Integer premise1;
	private Integer premise2;
	
	public ConjunctionIntro(){ super(); }

	public ConjunctionIntro(int premise1Index, int premise2Index){
		super();
		premise1 = premise1Index;
		premise2 = premise2Index;
	}
	
	@Override
    public void updateReference(int refNr, String refStr){
      if(refNr < 1 || refNr > 2) throw new IllegalArgumentException();
      Integer ref;
      try{
        ref = ReferenceParser.parseIntegerReference(refStr);
      }
      catch(NumberFormatException e){
        ref = null;
        throw new NumberFormatException(); //Still want this to propagate up
      }
      if(refNr == 1)setPremise1(ref);
      else/*refNr == 2*/ setPremise2(ref);
    }
	
	@Override
	public boolean hasCompleteInfo() {
		return premise1 != null && premise2 != null;
	}

	public Integer getPremise1() {
		return premise1;
	}

	public void setPremise1(int premise1) {
		this.premise1 = premise1;
	}

	public Integer getPremise2() {
		return premise2;
	}

	public void setPremise2(int premise2) {
		this.premise2 = premise2;
	}
	
	@Override
	public String toString(){
		String p1 = premise1 == null ? "" : premise1.toString();
		String p2 = premise2 == null ? "" : premise2.toString();
		return String.format("∧-I (%s),(%s)",p1,p2);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		//System.out.println("Verification.verifyAndIntro");
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check scope and if the data is verified
		if( data.isInScopeOf( getPremise1(), rowIndex) == false || //this currently assumes the premise is stored as a rowNr rather than index
				data.isInScopeOf( getPremise2(), rowIndex) == false )
		{
			System.out.println("    Scope issue");
			return false;
		}

		// do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Conjunction == false) return false;
		Conjunction conclusion = (Conjunction)rowToVerify.getFormula();
		Formula content1 = data.getRow( getPremise1() ).getFormula();//this currently assumes the premise is stored as a rowNr rather than index
		Formula content2 = data.getRow( getPremise2() ).getFormula();
		return conclusion.equals(new Conjunction(content1, content2));
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		return null;
	}

}
