package model.rules;

import model.Box;
import model.ProofRow;
import model.formulas.Conjunction;
import model.formulas.Formula;

public class ConjunctionElim implements Rule {
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
      }
      catch(NumberFormatException e){
        ref = null;
        throw new NumberFormatException(); //Still want this to propagate up
      }
      setPremise(ref);
    }
	
	@Override
	public boolean hasCompleteInfo() {
		//type variable is guaranteed in constructor
		return reference != null;
	}
	
	@Override
	public String toString(){
	  return String.format("ConjunctionElimRule%s  (%s)",type,reference);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		// is the rule object of the correct type? Probably just check with an assertion
		ProofRow rowToVerify = data.getRow( rowIndex );

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// Box.isInScope should check both scope and if the data is verified
		if( data.isInScopeOf( getPremise(), rowIndex ) == false ) return false;

		// do we have the needed references to make the deduction?
		Formula result = data.getRow(rowIndex).getFormula();
		Formula reference = data.getRow( getPremise() ).getFormula();
		if( reference instanceof Conjunction == false) return false;
		Conjunction ref = (Conjunction)reference;
		if( getType() == 1){
			return result.equals(ref.lhs);
		}
		else{ //rule.getType() == 2
			return result.equals(ref.rhs);
		}
	}

	@Override
	public Formula generateFormula(Box data, int rowIndex) {
		// is the rule object of the correct type? Probably just check with an assertion
		ProofRow rowToVerify = data.getRow( rowIndex );

		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// Box.isInScope should check both scope and if the data is verified
		if (!data.isInScopeOf(getPremise(), rowIndex)) return null;

		// do we have the needed references to make the deduction?
		Formula reference = data.getRow( getPremise() ).getFormula();
		if (!(reference instanceof Conjunction)) return null;
		Conjunction ref = (Conjunction)reference;
		if (getType() == 1){
			return ref.lhs;
		}
		else {
			return ref.rhs;
		}
	}
	
	@Override
	public String[] getReferenceStrings() {
		return new String[]{(reference+1) + ""};
	}

	@Override
	public String getDisplayName() {
		//Overridden in subclasses
		return "∧E";
	}
}
