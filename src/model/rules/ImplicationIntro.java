package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.Implication;

public class ImplicationIntro implements Rule {

	private Intervall premiseIntervall;

	public ImplicationIntro(){
		super();
	}
	
	public ImplicationIntro(Intervall indexIntervall){
		super();
		premiseIntervall = indexIntervall;
	}
	
	public Intervall getPremiseIntervall() {
		return premiseIntervall;
	}

	public void setPremiseIntervall(Intervall premiseIntervall) {
		this.premiseIntervall = premiseIntervall;
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return premiseIntervall != null;
	}
	
	@Override
	public String toString(){
		return String.format("→-I (%s)", premiseIntervall);
	}

	public boolean verify(Box data, int rowIndex) {
		//System.out.println("Verification.verifyImplicationIntro");
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );
		assert(rowToVerify.getRule() instanceof ImplicationIntro) :
				"Incorrect rule type function: Verification.verifyImplicationIntro";
		ImplicationIntro rule = (ImplicationIntro) rowToVerify.getRule();

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check both of these
		Intervall premiseIntervall = rule.getPremiseIntervall();
		if( data.isInScopeOf(premiseIntervall, rowIndex) == false) return false;

		//do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Implication == false) return false;
		Implication conclusion = (Implication)rowToVerify.getFormula();
		Formula assumption      = data.getRow( premiseIntervall.startIndex ).getFormula();
		Formula conclusionOfBox = data.getRow( premiseIntervall.endIndex ).getFormula();
		return conclusion.equals(new Implication(assumption, conclusionOfBox));

	}
}
