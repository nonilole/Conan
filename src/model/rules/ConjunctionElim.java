package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Conjunction;
import model.formulas.Formula;
import start.Constants;

public class ConjunctionElim extends Rule {
	private int type;
	private Integer reference;

	public ConjunctionElim(int type){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
	}

	public ConjunctionElim(int type, int premise){
		super();
		if( type != 1 && type != 2) throw new IllegalArgumentException("ConjunctionElim(int type): type must be 1 or 2");
		this.type = type;
		this.reference = premise;
	}

	public int getType() {
		return type;
	}

	public Integer getPremise() {
		return reference;
	}

	public void setPremise(Integer premise) {
		this.reference = premise;
	}

	@Override
	public void updateReference(int refNr, String refStr){
		if(refNr != 1) throw new IllegalArgumentException();
		Integer ref;
		try{
			ref = ReferenceParser.parseIntegerReference(refStr);
            setPremise(ref);
		}
		catch(NumberFormatException e){
			ref = null;
			setPremise(ref);
			throw new NumberFormatException(); //Still want this to propagate up
		}
	}

	@Override
	public boolean hasCompleteInfo() {
		//type variable is guaranteed in constructor
		return reference != null;
	}

	@Override
	public String toString(){
		return String.format("∧e_{%s}, %s", type, reference == null ? "" : new Integer(reference+1));
	}

	@Override
	public boolean verifyRow(Box data, int rowIndex) {
		Formula result = data.getRow(rowIndex).getFormula();
		Conjunction ref = (Conjunction) data.getRow( getPremise() ).getFormula();
		if(getType() == 1){
			return result.equals(ref.lhs);
		} else {
			return result.equals(ref.rhs);
		}
	}

	@Override
	public Formula generateRow(Box data) {
		Conjunction ref = (Conjunction) data.getRow( getPremise() ).getFormula();
		if(getType() == 1){
			return ref.lhs;
		} else {
			return ref.rhs;
		}
	}

	@Override
	public boolean verifyReferences(Box data, int rowIndex) {
		if( data.isInScopeOf( getPremise(), rowIndex ) == false ) return false;
		Formula reference = data.getRow( getPremise() ).getFormula();
		if( reference instanceof Conjunction == false) return false;
		return true;
	}

	@Override
	public String[] getReferenceStrings() {
		String ref1 = reference == null ? "" : (reference+1)+"";
		return new String[]{ref1};
	}

	@Override
	public String getDisplayName() {
		if (type == 1)
			return Constants.conjunctionElim1;
		else
			return Constants.conjunctionElim2;
	}
}
