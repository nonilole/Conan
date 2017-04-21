package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.Implication;

public class ImplicationElim implements Rule{
	
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
		
		//check that references are verified and in scope
		if(data.isInScopeOf(rowRef1, rowIndex) == false) return false;
		if(data.isInScopeOf(rowRef2, rowIndex) == false) return false;
		
		Formula referencedRow1 = data.getRow(rowRef1).getFormula();
		Formula referencedRow2 = data.getRow(rowRef2).getFormula();
		
		//make sure second reference is to an Implication formula and cast it
		if(referencedRow2 instanceof Implication == false) return false;
		Implication implRef = (Implication)referencedRow2;
		
		//check that the content of referenced rows and row to be verified are in line with the rule
		if(implRef.lhs.equals(referencedRow1) == false) return false;
		if(implRef.rhs.equals(data.getRow(rowIndex).getFormula()) == false) return false;
		
		return true;
	}
	
	@Override
	public String toString(){
		return "→E "+rowRef1+", "+rowRef2;
	}

}
