package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.Implication;

public class ImplicationIntro implements Rule {

	private Interval premiseInterval;

	public ImplicationIntro(){
		super();
	}
	
	public ImplicationIntro(Interval indexInterval){
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
    public void updateReference(int refNr, String refStr){
      if(refNr != 1) throw new IllegalArgumentException();
      Interval ref;
      try{
        ref = ReferenceParser.parseIntervalReference(refStr);
      }
      catch(NumberFormatException e){
        ref = null;
        throw new NumberFormatException(); //Still want this to propagate up
      }
      setPremiseInterval(ref);
    }
	
	@Override
	public boolean hasCompleteInfo() {
		return premiseInterval != null;
	}
	
	@Override
	public String toString(){
		return String.format("→-I (%s)", premiseInterval);
	}

	public boolean verify(Box data, int rowIndex) {
		//System.out.println("Verification.verifyImplicationIntro");
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check both of these
		Interval premiseInterval = getPremiseInterval();
		if( data.isInScopeOf(premiseInterval, rowIndex) == false) return false;

		//do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Implication == false) return false;
		Implication conclusion = (Implication)rowToVerify.getFormula();
		Formula assumption      = data.getRow( premiseInterval.startIndex ).getFormula();
		Formula conclusionOfBox = data.getRow( premiseInterval.endIndex ).getFormula();
		return conclusion.equals(new Implication(assumption, conclusionOfBox));

	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		//System.out.println("Verification.verifyImplicationIntro");
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check both of these
		Interval premiseInterval = getPremiseInterval();
		if( data.isInScopeOf(premiseInterval, rowIndex) == false) return null;

		//do we have the needed premises/references to make the deduction?
		Formula assumption      = data.getRow( premiseInterval.startIndex ).getFormula();
		Formula conclusionOfBox = data.getRow( premiseInterval.endIndex ).getFormula();
		return new Implication(assumption, conclusionOfBox);
	}
	
	@Override
	public String[] getReferenceStrings() {
		return new String[]{this.premiseInterval+""};
	}

	@Override
	public String getDisplayName() {
		return "→I";
	}
}
