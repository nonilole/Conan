package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.QuantifiedFormula;

public class ForallElim implements Rule{
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
		//System.out.println("ForallElim.verify("+rowIndex+")");
		assert(data.getRow(rowIndex).getRule() == this ) : "ForallElim.verify...";
		//check that reference is in scope and that it's verified
		data.isInScopeOf(rowRef, rowIndex);
		
		if(data.getRow(rowRef).getFormula() instanceof QuantifiedFormula == false) return false;
		QuantifiedFormula ref = (QuantifiedFormula)data.getRow(rowRef).getFormula();
		Formula toVerify = data.getRow(rowIndex).getFormula();
		return Formula.isInstantiationOf(toVerify, ref);
		
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		return null;
	}

	@Override
	public String toString(){
		return "∀E ("+rowRef+")";
	}
}
