
package model.formulas;

import java.lang.IllegalArgumentException;

public class QuantifiedFormula extends Formula{
    //since immutable, public not a problem
    public final Formula formula;
    public final String var;
    public final char type;

    public QuantifiedFormula(Formula formula, String var, char c){
        if(c != '∀' && c != '∃'){
            throw new IllegalArgumentException();
        }
        type = c; 
        this.formula = formula;
        this.var = var;
        super.precedence = 3;
    }
    
    @Override
    public Formula replace(String newId,String oldId){
    	return var.equals(oldId) ? this : new QuantifiedFormula(formula.replace(newId, oldId), var, type);
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof QuantifiedFormula){
    		QuantifiedFormula other = (QuantifiedFormula) o;
    		if(this.type != other.type) return false;
    		if(this.var != other.var) return false;
    		return this.formula.equals(other.formula);
    	}
    	return false;
    }
    
    @Override
    public String toString(){
    	StringBuilder strB = new StringBuilder();
    	strB.append(type+var);
    	strB.append( formula.getPrecedence() < 3 || formula instanceof Equality ? "("+formula+")" : formula+"");
    	return strB.toString();
    }
}
