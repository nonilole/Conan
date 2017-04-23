package model.rules;

import java.util.ArrayList;

import model.Box;
import model.formulas.Equality;
import model.formulas.Formula;

public class EqualityElim implements Rule{
	private Integer rowRef1;
	private Integer rowRef2;
	
	@Override
	public boolean hasCompleteInfo() {
		return rowRef1 != null && rowRef2 != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 2) throw new IllegalArgumentException();
		
		if(index == 1){
			try{
				rowRef1 = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef1 = null;
				throw new NumberFormatException();
			}
		}
		else{//index == 2
			try{
				rowRef2 = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef2 = null;
				throw new NumberFormatException();
			}
		}
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		//check that referenes are in scope
		if(data.isInScopeOf(rowRef1, rowIndex) == false) return false;
		if(data.isInScopeOf(rowRef2, rowIndex) == false) return false;
		
		//check that rowRef1-formula is an Equality
		if(data.getRow(rowRef1).getFormula() instanceof Equality == false) return false;
		Equality eq = (Equality) data.getRow(rowRef1).getFormula();
		
		//TODO: check that the only difference between formula at rowIndex and rowRef2 are 
		//instances where the left objectId is replaced by the right objectId from the equality
		Formula ref = data.getRow(rowRef2).getFormula();
		Formula toVerify = data.getRow(rowIndex).getFormula();
		return Formula.almostEqual(ref, toVerify, eq, new ArrayList<String>());
	}
	
	@Override
	public String toString(){
		return "=E "+rowRef1+", "+rowRef2;
	}

}
