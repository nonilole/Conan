
package model.formulas;

public class Disjunction extends Formula {
	public final Formula lhs;
    public final Formula rhs;

    public Disjunction(Formula lhs, Formula rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        super.precedence = 2;
    }
    
    @Override
    public Formula replace(String newId,String oldId){
    	return new Disjunction(lhs.replace(newId, oldId), rhs.replace(newId, oldId));
    }
    
    @Override
    public boolean equals(Object o){
    	if(o instanceof Disjunction){
    		Disjunction other = (Disjunction) o;
    		return this.lhs.equals(other.lhs) && this.rhs.equals(other.rhs);
    	}
    	return false;
    }
    
    @Override
    public String toString(){	
    	StringBuilder strB = new StringBuilder();
    	strB.append( lhs.getPrecedence() < 2 ? "("+lhs+")" : lhs+"" );
    	strB.append( " ∨ " );
    	strB.append( rhs.getPrecedence() < 3 ? "("+rhs+")" : rhs+"" );
    	return strB.toString();
    }
	@Override
	public String parenthesize(){
		StringBuilder strB = new StringBuilder();
		strB.append("("+lhs.parenthesize());
		strB.append( " ∨ " );
		strB.append(rhs.parenthesize() +")");
		return strB.toString();
	}

    @Override
	public boolean containsFreeObjectId(String id) {
		return lhs.containsFreeObjectId(id) ||  rhs.containsFreeObjectId(id);
	}

	@Override
	public Formula replace(Term newTerm, Term oldTerm) {
		return new Disjunction(lhs.replace(newTerm, oldTerm), rhs.replace(newTerm, oldTerm));
	}
}
