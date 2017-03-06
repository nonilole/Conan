
package model.formulas;

public class Negation extends Formula{
    public final Formula formula;
    
    public Negation(Formula formula){
    	this.formula = formula;
        super.precedence = 3;
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Negation){
    		Negation other = (Negation) o;
    		return this.formula.equals(other.formula);
    	}
    	return false;
    }
    
    @Override
    public String toString(){
    	return formula.getPrecedence() < 3 ? "¬("+formula+")" : "¬"+formula+"";
    }
}
