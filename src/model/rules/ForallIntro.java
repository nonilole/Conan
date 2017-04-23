package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import model.formulas.QuantifiedFormula;

public class ForallIntro implements Rule{
	private Interval intervalRef;

	@Override
	public boolean hasCompleteInfo() {
		return intervalRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index != 1) throw new IllegalArgumentException();
		try{
			intervalRef = ReferenceParser.parseIntervalReference(newValue);
		}
		catch(NumberFormatException e){
			intervalRef = null;
			throw new NumberFormatException();
		}
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		System.out.println("ForallIntro.verify("+rowIndex+")");
		// is the rule object of the correct type? Probably just check with an assertion
		assert(data.getRow(rowIndex).getRule() == this) : "ForallIntro: incorrect usage";
		
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		if( data.isInScopeOf(intervalRef, rowIndex) == false ) return false;
		//System.out.println("1");
		
		// does the referenced rows contain the data for this rule to be correct?
		Box refBox = data.getBox(intervalRef);
		//the box must contain a fresh var and a conclusion
		if( refBox.size() < 2) return false;
		if( refBox.getRow(0).getRule() instanceof FreshVar == false) return false;
		if( refBox.getRow(0).getFormula() instanceof FreshVarFormula == false) return false;
		String freshVarId = ((FreshVarFormula)refBox.getRow(0).getFormula()).var;
		//System.out.println("2");
		
		Formula lastRowInRefBox = refBox.getRow(refBox.size()-1).getFormula();
		QuantifiedFormula toVerify;
		if( data.getRow(rowIndex).getFormula() instanceof QuantifiedFormula == false) return false;
		//System.out.println("3");
		toVerify = (QuantifiedFormula)data.getRow(rowIndex).getFormula();
		//System.out.println("toVerify.instantiate: "+toVerify.instantiate(freshVarId));
		//System.out.println(""+lastRowInRefBox);
		return toVerify.instantiate(freshVarId).equals(lastRowInRefBox);
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		return null;
	}

	@Override
	public String toString(){
		return "∀I ("+intervalRef+")";
	}

}
