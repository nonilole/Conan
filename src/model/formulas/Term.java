
package model.formulas;

import java.util.ArrayList;
import java.util.List;

//needed for categorizing logical objects and functions as terms
public interface Term{
	public Term replace(String oldId, String newId);
	public boolean containsObjectId(String id);
	
	//return true if...rename?
	public static boolean equalOrSub(Term t1, Term t2, Equality eq, List<String> boundObjectIds){
		
		if(t1.getClass() != t2.getClass()) return false;
		
		else if(t1 instanceof LogicObject ){
			LogicObject o1 = (LogicObject) t1;
			LogicObject o2 = (LogicObject) t2;
			
			if(o1.equals(o2)) return true;
			
			else if(o1.equals( eq.lhs ) && 
					o2.equals( eq.rhs ) &&
					!boundObjectIds.contains(o1.id) &&
					!boundObjectIds.contains(o2.id) )
				return true;
			
			return false;
			
		}
		else if(t1 instanceof Function){
			Function f1 = (Function) t1;
			Function f2 = (Function) t2;
			List<Term> argsf1 = f1.getArgs();
			List<Term> argsf2 = f2.getArgs();
			
			
			if(argsf1.size() != argsf2.size() ) return false;
			for(int i = 0; i < argsf1.size(); i++){
				if( equalOrSub(argsf1.get(i), argsf2.get(i), eq, boundObjectIds) == false) return false;
			}
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
