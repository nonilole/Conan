package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;
import model.formulas.Negation;

public class ProofByContradiction implements Rule{

	private Interval interval;

	public ProofByContradiction() {
		super();
	}

	public ProofByContradiction(Interval interval) {
		super();
		this.interval = interval;
	}

	@Override
	public boolean hasCompleteInfo() {
		return interval != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 1) throw new IllegalArgumentException();

		if(index == 1){
			try{
				interval = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval = null;
				throw new NumberFormatException();
			}
		}
	}

	@Override
	public String toString(){
		return String.format("PBC (%s)", interval);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {

		//check the interval in scope
		if(data.isInScopeOf(interval, rowIndex) == false ) return false;

		//check if the end expression in the interval is a contradiction
		Formula intervalEndFormula = data.getRow(interval.endIndex).getFormula();
		if ( !(intervalEndFormula instanceof Contradiction) ) {
			return false;
		}

		Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();
		if (!(intervalStartFormula instanceof Negation))
			return false;
		Negation start = (Negation) intervalStartFormula;
		Formula RowToVerifyFormula = data.getRow(rowIndex).getFormula();
		if (!(start.formula.equals(RowToVerifyFormula)))
			return false;
		return true;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		//check the interval in scope
		if(data.isInScopeOf(interval, rowIndex) == false ) return null;

		//check if the end expression in the interval is a contradiction
		Formula intervalEndFormula = data.getRow(interval.endIndex).getFormula();
		if ( !(intervalEndFormula instanceof Contradiction) ) {
			return null;
		}

		Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();
		if (!(intervalStartFormula instanceof Negation))
			return null;
		Negation start = (Negation) intervalStartFormula;
		return start.formula;
	}

}
