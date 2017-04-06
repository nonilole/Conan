package model.rules;

import model.Box;

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
		System.out.println("ExistsIntro.verify("+rowIndex+")");
		// is the rule object of the correct type? Probably just check with an assertion
		assert(data.getRow(rowIndex).getRule() == this) : "ExistsIntro: incorrect usage";
		
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		if( data.isInScopeOf(this.rowRef, rowIndex) == false) return false;
		
		// does the referenced rows contain the data for this rule to be correct?
		//TODO:isInstantiationOf...
		return true;
	}
	
	@Override
	public String toString(){
		return "∃E ("+rowRef+")";
	}
}
