package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Formula;
import model.formulas.FreshVarFormula;
import model.formulas.QuantifiedFormula;

public class ExistsElim implements Rule{
	private Integer rowRef;
	private Interval intervalRef;
	
	@Override
	public boolean hasCompleteInfo(){
		return intervalRef != null && rowRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 2) throw new IllegalArgumentException();
		
		if(index == 1){
			try{
				rowRef = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef = null;
				throw new NumberFormatException();
			}
		}
		else{//index == 2
			try{
				intervalRef = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				intervalRef = null;
				throw new NumberFormatException();
			}
		}
		
	}
	
	@Override
	public String toString(){
		return String.format("∃E (%s) (%s)", rowRef, intervalRef);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		//System.out.println("ExistsElim.verify(data, "+rowIndex+")");
		// is the rule object of the correct type? Probably just check with an assertion
		assert( data.getRow(rowIndex).getRule() instanceof ExistsElim);//can skip this...
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		if( data.isInScopeOf(rowRef, rowIndex) == false )      return false;
		if( data.isInScopeOf(intervalRef, rowIndex) == false ) return false;
		
		//System.out.println("1");
		// is the referenced row of the correct type for this rule?
		ProofRow referencedRow = data.getRow(rowRef); 
		Formula referencedRowFormula = referencedRow.getFormula();
		if( referencedRowFormula instanceof QuantifiedFormula ){
			QuantifiedFormula quant = (QuantifiedFormula )referencedRowFormula;
			if(quant.type != '∃') return false;
		}
		else return false;
		//System.out.println("2");
		
		//does the box contain the needed data?
		QuantifiedFormula refdQuant = (QuantifiedFormula) referencedRowFormula;
		Box refBox = data.getBox(intervalRef);
		if(refBox.size() < 3) return false;//needs at least fresh var, instantiation of quantifiedFormula and conclusion not containing fresh var
		if(refBox.getRow(0).getRule() instanceof FreshVar == false) return false;
		//System.out.println("3");
		String freshVar = ((FreshVarFormula)refBox.getRow(0).getFormula()).var;
		//second row needs to be the quantified formula instantiated with the fresh variable
		//System.out.println("Instantiated: "+refdQuant.instantiate(freshVar));
		//System.out.println("Compared to: "+refBox.getRow(1).getFormula());
		if( refBox.getRow(1).getFormula().equals(refdQuant.instantiate(freshVar)) == false) return false;
		//System.out.println("4");
		//last row in box needs to equal the formula in the referencingRow
		if(data.getRow(rowIndex).getFormula().equals( refBox.getRow(refBox.size()-1).getFormula()) == false) return false;
		//System.out.println("5");
		//last row in box cant contain the freshVar
		if(refBox.getRow(refBox.size()-1).getFormula().containsObjectId(freshVar)) return false;
		return true;
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		if( data.isInScopeOf(rowRef, rowIndex) == false )      return null;
		if( data.isInScopeOf(intervalRef, rowIndex) == false ) return null;
		ProofRow referencedRow = data.getRow(rowRef);
		Formula referencedRowFormula = referencedRow.getFormula();
		if( referencedRowFormula instanceof QuantifiedFormula ){
			QuantifiedFormula quant = (QuantifiedFormula )referencedRowFormula;
			if(quant.type != '∃') return null;
		}
		else return null;
		QuantifiedFormula refdQuant = (QuantifiedFormula) referencedRowFormula;
		Box refBox = data.getBox(intervalRef);
		if(refBox.size() < 3) return null;//needs at least fresh var, instantiation of quantifiedFormula and conclusion not containing fresh var
		if(refBox.getRow(0).getRule() instanceof FreshVar == false) return null;
		String freshVar = ((FreshVarFormula)refBox.getRow(0).getFormula()).var;
		if( refBox.getRow(1).getFormula().equals(refdQuant.instantiate(freshVar)) == false) return null;
		if(refBox.getRow(refBox.size()-1).getFormula().containsObjectId(freshVar)) return null;
		return refBox.getRow(refBox.size()-1).getFormula();
	}
	
	@Override
	public String[] getReferenceStrings() {
		return new String[]{rowRef+"", intervalRef+""};
	}

	@Override
	public String getDisplayName() {
		return "∃E";
	}
}
