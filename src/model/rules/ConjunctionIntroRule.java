package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Conjunction;
import model.formulas.Formula;

public class ConjunctionIntroRule implements Rule{

	//indexes of the premises
	private Integer premise1;
	private Integer premise2;
	
	public ConjunctionIntroRule(){ super(); }
	
	public ConjunctionIntroRule(int premise1Index, int premise2Index){
		super();
		premise1 = premise1Index;
		premise2 = premise2Index;
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
		assert(rowToVerify.getRule() instanceof ConjunctionIntroRule):"Incorrect rule type in Verification.verifyConjunctionIntro";
		ConjunctionIntroRule rule = (ConjunctionIntroRule) rowToVerify.getRule();

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check scope and if the data is verified
		if( data.isInScopeOf( rule.getPremise1()-1, rowIndex) == false || //this currently assumes the premise is stored as a rowNr rather than index
				data.isInScopeOf( rule.getPremise2()-1, rowIndex) == false )
		{
			System.out.println("    Scope issue");
			return false;
		}

		// do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Conjunction == false) return false;
		Conjunction conclusion = (Conjunction)rowToVerify.getFormula();
		Formula premise1 = data.getRow( rule.getPremise1()-1 ).getFormula();//this currently assumes the premise is stored as a rowNr rather than index
		Formula premise2 = data.getRow( rule.getPremise2()-1 ).getFormula();
		return conclusion.equals(new Conjunction(premise1, premise2));
	}
	
}
