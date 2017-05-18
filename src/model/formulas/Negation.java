
package model.formulas;

public class Negation extends Formula{
    public final Formula formula;
    
    public Negation(Formula formula){
    	this.formula = formula;
        super.precedence = 3;
    }
    
    @Override
    public Formula replace(String newId,String oldId){
    	return new Negation(formula.replace(newId, oldId));
    }
    
    @Override
	public Formula replace(Term newTerm, Term oldTerm) {
    	return new Negation(formula.replace(newTerm, oldTerm));
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

    @Override
    public String parenthesize() {
        return "(¬"+formula.parenthesize()+")";
    }

    @Override
	public boolean containsFreeObjectId(String id) {
		return formula.containsFreeObjectId(id);
	}

}
