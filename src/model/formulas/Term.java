
package model.formulas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//needed for categorizing logical objects and functions as terms
public interface Term extends Serializable{
	public Term replace(String oldId, String newId);
	public boolean containsObjectId(String id);
	
	//return true if...rename?
	public static boolean equalOrSub(Term t1, Term t2, Equality eq, List<LogicObject> boundObjects){
		
		//if(t1.getClass() != t2.getClass()) return false;
		if(t1.equals(t2)) return true;
		
		if( t1.equals(eq.lhs) && t2.equals(eq.rhs)){
			
			if(boundObjects.contains(t1) || boundObjects.contains(t2)) return false;
			return true;
		}
		
		return false;
	}
	
	public static String[] getIdDifference(Term t1, Term t2){
		if(t1.getClass() != t2.getClass()) return null;
		else if(t1 instanceof LogicObject ){
			String[] res = {((LogicObject)t1).id, ((LogicObject)t2).id };
			if(res[0].equals(res[1]) == false) return res;
			else return null;
		}else if(t1 instanceof Function){
			Function f1 = (Function) t1;
			Function f2 = (Function) t2;
			if(f1.getArgs().size() != f2.getArgs().size() ) return null;
			for(int i = 0; i < f1.getArgs().size(); i++){
				String[] res = Term.getIdDifference(f1.getArgs().get(i), f2.getArgs().get(i));
				if(res != null) return res;
			}
		}
		return null;
	}
}
