package model.rules;

import model.Box;
import model.formulas.Formula;
import model.formulas.Negation;

public class DoubleNegationElim implements Rule {
    private Integer premise1;
    public DoubleNegationElim() {}
    public DoubleNegationElim(int premise1Index) {
        this.premise1 = premise1Index;
    }
    public Integer getPremise1() {
        return premise1;
    }

    public void setPremise1(int premise1) {
        this.premise1 = premise1;
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
        setPremise1(ref);
    }

    @Override
    public boolean hasCompleteInfo() {
        return premise1 != null;
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        if (data.isInScopeOf(getPremise1(), rowIndex) == false) return false;
        Formula premise = data.getRow(getPremise1()).getFormula();
        if (premise instanceof Negation == false) {
            return false;
        }
        Negation negation = (Negation) premise;
        premise = negation.formula;
        if (premise instanceof Negation == false) {
            return false;
        }
        negation = (Negation) premise;
        return negation.formula.equals(data.getRow(rowIndex).getFormula());
    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        if (data.isInScopeOf(getPremise1(), rowIndex) == false) return null;
        Formula premise = data.getRow(getPremise1()).getFormula();
        if (premise instanceof Negation == false) {
            return null;
        }
        Negation negation = (Negation) premise;
        premise = negation.formula;
        if (premise instanceof Negation == false) {
            return null;
        }
        negation = (Negation) premise;
        return negation.formula;
    }

    @Override
    public String toString(){
        String p1 = premise1 == null ? "" : premise1.toString();
        return String.format("¬¬-E (%s)", p1);
    }
    
    @Override
	public String[] getReferenceStrings() {
		return new String[]{(premise1+1)+""};
	}
    
	@Override
	public String getDisplayName() {
		return "¬¬E";
	}
}
