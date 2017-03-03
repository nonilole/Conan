
package model.formulas;

public class Equality extends Formula {
    public final Term lhs;
    public final Term rhs;

    public Equality(Term lhs, Term rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 3;
    }

    //TODO: equals()
    
    @Override
    public String toString(){	
    	return lhs+" = "+rhs;
    }

}
