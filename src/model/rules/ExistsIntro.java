package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;

public class ExistsIntro implements Rule{
	private Integer rowRef;

	@Override
	public boolean hasCompleteInfo() {
		return rowRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index != 1) throw new IllegalArgumentException();
		try{
			rowRef = ReferenceParser.parseIntegerReference(newValue);
		}
		catch(NumberFormatException e){
			rowRef = null;
			throw new NumberFormatException();
		}
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		//System.out.println("ExistsIntro.verify("+rowIndex+")");
		// is the rule object of the correct type? Probably just check with an assertion
		assert(data.getRow(rowIndex).getRule() == this) : "ExistsIntro: incorrect usage";
		
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		if( data.isInScopeOf(this.rowRef, rowIndex) == false) return false;
		
		// does the referenced rows contain the data for this rule to be correct?
		Formula toVerify = data.getRow(rowIndex).getFormula();
		Formula ref = data.getRow(this.rowRef).getFormula();
		if( toVerify instanceof QuantifiedFormula ){
			return Formula.isInstantiationOf(ref, (QuantifiedFormula)toVerify);
		} 
		return false;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		assert(data.getRow(rowIndex).getRule() == this) : "ExistsIntro: incorrect usage";
		if( data.isInScopeOf(this.rowRef, rowIndex) == false) return null;
		Formula ref = data.getRow(this.rowRef).getFormula();
		return new QuantifiedFormula(ref,"x",'∃'); // Maybe not x?
	}

	@Override
	public String toString(){
		return "∃E ("+rowRef+")";
	}
	
	@Override
	public String[] getReferenceStrings() {
		String ref1 = rowRef == null ? "" : (rowRef+1)+"";
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
		return "∃I";
	}
}
