package model.rules;

import model.Box;
import model.formulas.Contradiction;
import model.formulas.Formula;

public class ContradictionElim implements Rule {

    private Integer premise;

    public ContradictionElim(){

    }
    public ContradictionElim(Integer premise){
        this.premise=premise;
    }

    @Override
    public boolean hasCompleteInfo() {
        return premise!=null;
    }

    @Override
    public void updateReference(int refNr, String refStr){
        if(refNr != 1) throw new IllegalArgumentException();
        Integer ref;
        try{
            ref = ReferenceParser.parseIntegerReference(refStr);
        }
        catch(NumberFormatException e) {
            ref = null;
            throw new NumberFormatException(); //Still want this to propagate up
        }
        setPremise(ref);
    }


    @Override
    public String toString(){
        return String.format("⊥E (%s)", premise);
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        Formula result = data.getRow(rowIndex).getFormula();
        Formula premise = data.getRow(getPremise()).getFormula();

        // are the references in the rule object in scope of rowIndex?
        // are all the referenced rows verified?
        // ProofData.isInScope should check both of these
        if( data.isInScopeOf(getPremise(), rowIndex) == false) return false;

        if(premise instanceof Contradiction ) return true;
        return false;


    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        return null;
    }

    public Integer getPremise() {
        return premise;
    }


    public void setPremise(Integer premise) {
        this.premise = premise;
    }
    
	@Override
	public String[] getReferenceStrings() {
		return new String[]{ premise == null ? "" : (premise+1)+""};
	}
	@Override
	public String getDisplayName() {
		return "⊥E";
	}
    
    
}
