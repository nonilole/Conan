package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.Implication;
import model.formulas.Negation;

public class ModusTollens implements Rule {
	
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
		Formula result = data.getRow(rowIndex).getFormula();
        if(referencedRow1 instanceof Negation == false) return false;
		if(result instanceof Negation == false) return false;
		Negation negRef = (Negation) referencedRow1;
		if (!implRef.rhs.equals(negRef.formula)) return false;
		Negation negResult = (Negation) result;
		if (!implRef.lhs.equals(negResult.formula)) return false;
		return true;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		//check that references are verified and in scope
		if(data.isInScopeOf(rowRef1, rowIndex) == false) return null;
		if(data.isInScopeOf(rowRef2, rowIndex) == false) return null;

		Formula referencedRow1 = data.getRow(rowRef1).getFormula();
		Formula referencedRow2 = data.getRow(rowRef2).getFormula();

		//make sure second reference is to an Implication formula and cast it
		if(referencedRow2 instanceof Implication == false) return null;
		Implication implRef = (Implication)referencedRow2;

		//check that the content of referenced rows and row to be verified are in line with the rule
		if(referencedRow1 instanceof Negation == false) return null;
		Negation negRef = (Negation) referencedRow1;
		if (!implRef.rhs.equals(negRef.formula)) return null;
		return new Negation(implRef.lhs);
	}

	@Override
	public String toString(){
		return "MT "+rowRef1+", "+rowRef2;
	}
	
	@Override
	public String[] getReferenceStrings() {
		return new String[]{(rowRef1+1)+"", (rowRef2+1)+""};
	}

	@Override
	public String getDisplayName() {
		return "MT";
	}

}
