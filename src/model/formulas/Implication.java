
package model.formulas;

public class Implication extends Formula {
	public final Formula lhs;
    public final Formula rhs;

    public Implication(Formula lhs, Formula rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 1;
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Implication){
    		Implication other = (Implication) o;
    		return this.lhs.equals(other.lhs) && this.rhs.equals(other.rhs);
    	}
    	return false;
    }
    
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
