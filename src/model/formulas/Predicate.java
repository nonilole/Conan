
package model.formulas;

import java.util.List;
import java.util.ArrayList;

public class Predicate extends Formula{
    public final String id;
    private List<Term> args;

    public Predicate(String id, List<Term> args){
        this.id = id;
        this.args = args;
        super.precedence = 3;
    }
    
    @Override
    public Formula replace(String newId,String oldId){
    	List<Term> newArgs = new ArrayList<Term>();
    	for(Term t : args){
    		newArgs.add(t.replace(newId, oldId));
    	}
    	return new Predicate(id, newArgs);
    }
    
    @Override
	public Formula replace(Term newTerm, Term oldTerm) {
    	List<Term> newArgs = new ArrayList<Term>();
    	for(Term t : args){
    		//newArgs.add(t.replace(newId, oldId));
    		Term term = t.equals(oldTerm) ? newTerm : t;
    		newArgs.add(term);
    	}
    	return new Predicate(id, newArgs);
	}

    public List<Term> getArgs(){
        return new ArrayList<Term>(args);
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Predicate){
    		Predicate p = (Predicate) o;
    		if(!this.id.equals(p.id)) return false;
    		if(this.args.size() != p.args.size()) return false;
    		for(int i = 0; i < this.args.size(); i++){
    			if(!this.args.get(i).equals(p.args.get(i))) return false;
    		}
    		return true;
    	}
    	return false;
    }
    
    @Override
    public String toString(){
        StringBuilder strB = new StringBuilder();
        strB.append( id );
        if(args.size() > 0){
        	strB.append("(");
        	for(int i = 0; i < args.size(); i++){
        		strB.append(args.get(i));
        		if(i < args.size() - 1 )
        			strB.append(", ");
        	}
        	strB.append(")");
        }
        return strB.toString();
    }

	@Override
	public String parenthesize(){
        return toString();
	}

    @Override
	public boolean containsFreeObjectId(String id) {
    	for(Term arg : args){
			if(arg.containsObjectId(id)) return true;
		}
		return false;
	}
}
