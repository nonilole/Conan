
package model.formulas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Use interface instead?
public abstract class Formula implements Serializable{
	protected int precedence;
	public int getPrecedence(){
		return precedence;
	}
	
	//return new formula that has replaced, for all free LogicObjects with id=oldId, with newId, 
	public abstract Formula replace(String newId,String oldId);
	
	public abstract boolean containsObjectId(String id);
	
	public static boolean isInstantiationOf(Formula instantiation, QuantifiedFormula quant){ 
		String[] diff = findObjectIdDifference(instantiation, quant.formula);
		if(diff == null) {
			return instantiation.equals(quant.formula);
		}
		else {
			//System.out.println("diff[0]: "+diff[0]+", diff[1]: "+diff[1]);
			return instantiation.equals(quant.formula.replace(diff[0], diff[1]));
		}
	}
	
	//check that two formulas are equal except for substitutions permitted by the eq argmunet
	public static boolean almostEqual(Formula f1, Formula f2, Equality eq, List<String> boundObjectIds){
		if(f1.getClass() != f2.getClass()) {
			//System.err.println(formula1.getClass()+" != "+formula2.getClass());
			return false;
		}
		else if(f1 instanceof Conjunction){
			return almostEqual(((Conjunction)f1).lhs,((Conjunction)f2).lhs, eq, boundObjectIds) 
				&& almostEqual(((Conjunction)f1).rhs,((Conjunction)f2).rhs, eq, boundObjectIds) ;
		}
		else if(f1 instanceof Disjunction){
			return almostEqual(((Disjunction)f1).lhs,((Disjunction)f2).lhs, eq, boundObjectIds) 
				&& almostEqual(((Disjunction)f1).rhs,((Disjunction)f2).rhs, eq, boundObjectIds) ;
		}
		else if(f1 instanceof Implication){
			return almostEqual(((Implication)f1).lhs, ((Implication)f2).lhs, eq, boundObjectIds) 
				&& almostEqual(((Implication)f1).rhs, ((Implication)f2).rhs, eq, boundObjectIds) ;
		}
		else if(f1 instanceof Equality){
			Equality eql = (Equality)f1;
			Equality eqr = (Equality)f2;
			boolean lhs = Term.equalOrSub(eql.lhs, eqr.lhs, eq, boundObjectIds);
			boolean rhs = Term.equalOrSub(eql.rhs, eqr.rhs, eq, boundObjectIds);
			return lhs && rhs;
		}
		else if(f1 instanceof Negation){
			return almostEqual( ((Negation)f1).formula, ((Negation)f2).formula, eq, boundObjectIds);
		}
		else if(f1 instanceof QuantifiedFormula){
			ArrayList<String> newBoundObjectIds = new ArrayList<String>(boundObjectIds);
			newBoundObjectIds.add( ((QuantifiedFormula)f1).var );
			return almostEqual( ((QuantifiedFormula)f1).formula, ((QuantifiedFormula)f2).formula, eq, newBoundObjectIds);
		}
		else if(f1 instanceof Predicate){
			Predicate p1 = (Predicate) f1;
			Predicate p2 = (Predicate) f2;
			List<Term> p1args = p1.getArgs();
			List<Term> p2args = p2.getArgs();
			
			if(p1args.size() != p2args.size() ) return false;
			for(int i = 0; i < p1args.size(); i++){
				boolean logEq = Term.equalOrSub(p1args.get(i), p2args.get(i), eq, boundObjectIds);
				if( !logEq ) return false;
			}
			return true;
		}
		else{
			System.err.println("Formula.findObjectIdDifference: non-exhaustive matching for class:"+f1.getClass().getSimpleName());
		}
		return false;
	}
	
	/**
	 * Should return an Array of size 2 if there if both input parameters are LogicObjects
	 * and their id's are different. If the input formulas are of different classes, return null.
	 * If the input formulas are the same class bot not LogicObjects, recursively check their subformulas.
	 * @param formula1
	 * @param formula2
	 * @return
	 */
	public static String[] findObjectIdDifference(Formula formula1, Formula formula2){
		if(formula1.getClass() != formula2.getClass()) {
			//System.err.println(formula1.getClass()+" != "+formula2.getClass());
			return null;
		}
		else if(formula1 instanceof Conjunction){
			String[] leftResult = findObjectIdDifference(((Conjunction)formula1).lhs, ((Conjunction)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findObjectIdDifference(((Conjunction)formula1).rhs, ((Conjunction)formula2).rhs );
		}
		else if(formula1 instanceof Disjunction){
			String[] leftResult = findObjectIdDifference(((Disjunction)formula1).lhs, ((Disjunction)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findObjectIdDifference(((Disjunction)formula1).rhs, ((Disjunction)formula2).rhs );
		}
		else if(formula1 instanceof Implication){
			String[] leftResult = findObjectIdDifference(((Implication)formula1).lhs, ((Implication)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findObjectIdDifference(((Implication)formula1).rhs, ((Implication)formula2).rhs );
		}
		else if(formula1 instanceof Equality){
			Equality f1 = (Equality) formula1;
			Equality f2 = (Equality) formula2;
			String [] lhs = Term.getIdDifference(f1.lhs, f2.lhs);
			if(lhs != null) return lhs;
			else return Term.getIdDifference(f1.rhs, f2.rhs);
		}
		else if(formula1 instanceof Negation){
			Negation f1 = (Negation) formula1;
			Negation f2 = (Negation) formula2;
			return findObjectIdDifference(f1.formula, f2.formula);
		}
		else if(formula1 instanceof QuantifiedFormula){
			QuantifiedFormula f1 = (QuantifiedFormula) formula1;
			QuantifiedFormula f2 = (QuantifiedFormula) formula2;
			if(f1.type != f2.type) return null;
			else return findObjectIdDifference(f1.formula, f2.formula);
		}
		else if(formula1 instanceof Predicate){
			Predicate f1 = (Predicate) formula1;
			Predicate f2 = (Predicate) formula2;
			if(f1.getArgs().size() != f2.getArgs().size() ) return null;
			for(int i = 0; i < f1.getArgs().size(); i++){
				String[] res = Term.getIdDifference(f1.getArgs().get(i), f2.getArgs().get(i));
				if(res != null) return res;
			}
			return null;
		}
		else{
			System.err.println("Formula.findObjectIdDifference: non-exhaustive matching for class:"+formula1.getClass().getSimpleName());
		}
		return null;
	}
}

