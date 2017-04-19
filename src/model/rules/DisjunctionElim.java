package model.rules;

import model.Box;
import model.formulas.Disjunction;
import model.formulas.Formula;

public class DisjunctionElim implements Rule{

	private Integer rowRef;
	private Interval interval1;
	private Interval interval2;

	public DisjunctionElim() {
		super();
	}

	public DisjunctionElim(Integer rowRef, Interval interval1, Interval interval2) {
		super();
		this.rowRef = rowRef;
		this.interval1 = interval1;
		this.interval2 = interval2;
	}

	@Override
	public boolean hasCompleteInfo() {
		return rowRef != null && interval1 != null && interval2 != null;
	}

	/**
	 * Updates the values for the three rule prompts. 
	 * @param index: the index for the rule prompt box.
	 * @param newValue: the new value for that specific rule prompt box. 
	 */
	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 3) throw new IllegalArgumentException();

		if(index == 1){
			try{
				rowRef = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef = null;
				throw new NumberFormatException();
			}
		} else if (index == 2){
			try{
				interval1 = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval1 = null;
				throw new NumberFormatException();
			}
		} else {
			try{
				interval2 = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval2 = null;
				throw new NumberFormatException();
			}
		}
	}
	
	@Override
	public String toString(){
		//behövs något liknande här? 
		//String p1 = premise1 == null ? "" : premise1.toString();
		
		return String.format("∨E (%s) (%s) (%s)", rowRef, interval1, interval2);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		
		//check the row and the two intervals in scope
		if(data.isInScopeOf(rowRef, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval1, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval2, rowIndex) == false ) return false;
		
		//check if the expression in the referenced row is an or-statement
		Formula referencedRowFormula = data.getRow(rowRef).getFormula();
		if( !(referencedRowFormula instanceof Disjunction) ) {
			return false;
		}
		
		//save the rhs and lhs of the disjunction expression in the row
		Disjunction disj = (Disjunction)referencedRowFormula;
		Formula lhsDisj = disj.lhs;
		Formula rhsDisj = disj.rhs;
		
		//check if the start expression in the referenced interval1 and interval2 is correct  
		Formula interval1StartFormula = data.getRow(interval1.startIndex).getFormula();	
		Formula interval2StartFormula = data.getRow(interval2.startIndex).getFormula();
		if(interval1StartFormula != lhsDisj || interval1StartFormula != rhsDisj) {
			return false;
		} else if(interval1StartFormula == lhsDisj) {
			if (interval2StartFormula != rhsDisj) {
				return false;
			}
		} else if(interval1StartFormula == rhsDisj) {
			if (interval2StartFormula != lhsDisj) {
				return false;
			}
		}
		
		//check if the end expression in the referenced interval1 and interval2 is correct
		Formula interval1EndFormula = data.getRow(interval1.endIndex).getFormula();	
		Formula interval2EndFormula = data.getRow(interval2.endIndex).getFormula();
		if (!(interval1EndFormula == data.getRow(rowIndex).getFormula() &&
				interval2EndFormula == data.getRow(rowIndex).getFormula())) {
			return false;
		}
		return true;
	}

}
