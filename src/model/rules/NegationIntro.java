package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;
import model.formulas.Negation;

public class NegationIntro implements Rule{

	private Interval interval;

	public NegationIntro() {
		super();
	}

	public NegationIntro(Interval interval) {
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
		return String.format("¬I (%s)", interval);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {

		//check the interval in scope
		if(data.isInScopeOf(interval, rowIndex) == false ) return false;

		//check if the expression in the row to verify is a negation-statement
		Formula RowToVerifyFormula = data.getRow(rowIndex).getFormula();
		if( !(RowToVerifyFormula instanceof Negation) ) {
			return false;
		}

		//check if the end expression in the interval is a contradiction
		Formula intervalEndFormula = data.getRow(interval.endIndex).getFormula();
		if ( !(intervalEndFormula instanceof Contradiction) ) {
			return false;
		}

		//Check if the start expression in the interval is 
		//the negation of the expression in the row to verify
		Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();
		Negation neg = (Negation) RowToVerifyFormula;
		Formula RowToVerifyFormulaNoNeg = neg.formula;
		if(!intervalStartFormula.equals(RowToVerifyFormulaNoNeg)) {
			return false;
		}

		return true;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		if(data.isInScopeOf(interval, rowIndex) == false ) return null;

		//check if the end expression in the interval is a contradiction
		Formula intervalEndFormula = data.getRow(interval.endIndex).getFormula();
		if ( !(intervalEndFormula instanceof Contradiction) ) {
			return null;
		}

		//Check if the start expression in the interval is
		//the negation of the expression in the row to verify
		Formula intervalStartFormula = data.getRow(interval.startIndex).getFormula();

		return new Negation(intervalStartFormula);
	}

	@Override
	public String[] getReferenceStrings() {
		return new String[]{this.interval+""};
	}

	@Override
	public String getDisplayName() {
		return "¬I";
	}
}
