
package model.formulas;

public class Equality extends Formula {
    public final Term lhs;
    public final Term rhs;

    public Equality(Term lhs, Term rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 3;
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Equality){
    		Equality e = (Equality)o;
    		return this.lhs.equals(e.lhs) && this.rhs.equals(e.rhs);
    	}
    	return false;
    }
    
    @Override
    public String toString(){	
    	return lhs+" = "+rhs;
    }

}