
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

    //TODO: equals()
    @Override
    public String toString(){
    	StringBuilder strB = new StringBuilder();
    	strB.append(type+var);
    	strB.append( formula.getPrecedence() < 3 ? "("+formula+")" : formula+"");
    	return strB.toString();
    }
}
