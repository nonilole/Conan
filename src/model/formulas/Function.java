
package model.formulas;

import java.util.ArrayList;
import java.util.List;

public class Function implements Term{
    public final String id;
    private List<Term> args;
    
    public Function(String id, List<Term> args){
        this.id = id;
        this.args = args;
    }
    
    @Override
    public Term replace(String newId,String oldId){
    	List<Term> newArgs = new ArrayList<Term>();
    	for(Term t : args){
    		newArgs.add(t.replace(newId, oldId));
    	}
    	return new Function(id, newArgs);
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof Function){
    		Function f = (Function) o;
    		if(!this.id.equals(f.id)) return false;
    		if(this.args.size() != f.args.size()) return false;
    		for(int i = 0; i < this.args.size(); i++){
    			if(!this.args.get(i).equals(f.args.get(i))) return false;
    		}
    		return true;
    	}
    	return false;
    }
    
    public List<Term> getArgs(){
        return new ArrayList<Term>(args);
    }

    public String toString(){
        StringBuilder strB = new StringBuilder();
        strB.append( id );
        strB.append("(");
        for(int i = 0; i < args.size(); i++){
            strB.append(args.get(i));
            if(i < args.size() - 1 )
                strB.append(", ");
        }
        strB.append(")");
        return strB.toString();
    }

    public String parenthesize(){
        return toString();
    }

	@Override
	public boolean containsObjectId(String id) {
		for(Term arg : args){
			if(arg.containsObjectId(id)) return true;
		}
		return false;
	}
    
}
