
package model.formulas;

public class Implication extends Formula {
	public final Formula lhs;
    public final Formula rhs;

    public Implication(Formula lhs, Formula rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 1;
    }

    //TODO: equals()
    
    @Override
    public String toString(){
    	if(lhs instanceof Implication){
    		return "("+lhs+")"+" → "+rhs;
    	}
    	else{
    		return lhs+" → "+rhs;
    	}
    }
}
