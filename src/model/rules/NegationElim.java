package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Contradiction;
import model.formulas.Formula;
import model.formulas.Negation;

public class NegationElim implements Rule{

	private Integer rowRef1;
	private Integer rowRef2;

	public NegationElim() {
		super();
	}
	
	public NegationElim(Integer rowRef1, Integer rowRef2) {
		super();
		this.rowRef1 = rowRef1;
		this.rowRef2 = rowRef2;
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return rowRef1 != null && rowRef2 != null;
	}

	/**
	 * Updates the values for the three rule prompts. 
	 * @param index: the index for the rule prompt box.
	 * @param newValue: the new value for that specific rule prompt box.
	 */
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
	public String toString(){
		String p1 = rowRef1 == null ? "" : rowRef1.toString();
		String p2 = rowRef2 == null ? "" : rowRef2.toString();
		return String.format("¬E (%s),(%s)",p1,p2);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		
		//check the rows are in scope
		if(data.isInScopeOf(rowRef1, rowIndex) == false ) return false;
		if(data.isInScopeOf(rowRef2, rowIndex) == false ) return false;
		
		//Check if the formula in the row to verify i a contradiction
		ProofRow rowToVerify = data.getRow(rowIndex);
		Formula rowToVerifyFormula = data.getRow(rowIndex).getFormula();
		if( !(rowToVerifyFormula instanceof Contradiction) ) {
			return false;
		}
		
		//Check if the to referenced rows are the negation of each other 
		Formula rowRef1Formula = data.getRow(rowRef1).getFormula();
		Formula rowRef2Formula = data.getRow(rowRef2).getFormula();
		if(!(rowRef2Formula instanceof Negation)) {
			return false;
		}
		Negation neg = (Negation) rowRef2Formula;
		Formula rowRef2NoNeg = neg.formula;
		if(!rowRef2NoNeg.equals(rowRef1Formula)) {
			return false;
		}
		
		return true;
	}
}
