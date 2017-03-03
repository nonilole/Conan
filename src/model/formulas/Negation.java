
package model.formulas;

public class Negation extends Formula{
    public final Formula formula;
    
    public Negation(Formula formula){
    	this.formula = formula;
        super.precedence = 3;
    }

    //TODO: equals()
    
    @Override
    public String toString(){
    	return formula.getPrecedence() < 3 ? "¬("+formula+")" : "¬"+formula+"";
    }
}
