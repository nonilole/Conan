
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
    public Formula replace(String newId,String oldId){
    	return new Equality(lhs.replace(newId, oldId), rhs.replace(newId, oldId));
    }
    
    @Override
    public Formula replace(Term newTerm,Term oldTerm){
    	Term lhsRet = this.lhs.equals(oldTerm) ? newTerm : oldTerm;
    	Term rhsRet = this.rhs.equals(oldTerm) ? newTerm : oldTerm;
    	return new Equality(lhsRet, rhsRet);
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
    @Override
    public String parenthesize() {
        return toString();
    }


        @Override
	public boolean containsFreeObjectId(String id) {
		return lhs.containsObjectId(id) ||  rhs.containsObjectId(id);
	}

}
