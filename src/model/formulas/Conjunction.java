
package model.formulas;

public class Conjunction extends Formula {
    public final Formula lhs;
    public final Formula rhs;

    public Conjunction(Formula lhs, Formula rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 2;
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Conjunction){
    		Conjunction other = (Conjunction) o;
    		return this.lhs.equals(other.lhs) && this.rhs.equals(other.rhs);
    	}
    	return false;
    }
    
    @Override
    public Formula replace(String newId,String oldId){
    	return new Conjunction(lhs.replace(newId, oldId), rhs.replace(newId, oldId));
    }
    
    @Override
    public String toString(){	
    	StringBuilder strB = new StringBuilder();
    	strB.append( lhs.getPrecedence() < 2 ? "("+lhs+")" : lhs+"" );
    	strB.append( " ∧ " );
    	strB.append( rhs.getPrecedence() < 3 ? "("+rhs+")" : rhs+"" );
    	return strB.toString();
    }
}
