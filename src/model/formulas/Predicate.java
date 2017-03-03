
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

    public List<Term> getArgs(){
        return new ArrayList<Term>(args);
    }

    //TODO: equals()
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
}
