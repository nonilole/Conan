
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
	
	public abstract Formula replace(Term newTerm,Term oldTerm);
	
	public abstract boolean containsFreeObjectId(String id);
	public abstract String parenthesize();

	public static boolean isInstantiationOf(Formula instantiation, QuantifiedFormula quant){ 
		Term[] diff = findTermDifference(instantiation, quant.formula);
		if(diff == null) {
			return instantiation.equals(quant.formula);
		}
		else {
			//System.out.println("diff[0]: "+diff[0]+", diff[1]: "+diff[1]);
			return instantiation.equals(quant.formula.replace(diff[0], diff[1]));
		}
	}
	
	//check that two formulas are equal except for substitutions permitted by the eq argmunet
	public static boolean almostEqual(Formula f1, Formula f2, Equality eq, List<LogicObject> boundObjects){
		if(f1.getClass() != f2.getClass()) {
			//System.err.println(formula1.getClass()+" != "+formula2.getClass());
			return false;
		}
		else if(f1 instanceof Conjunction){
			return almostEqual(((Conjunction)f1).lhs,((Conjunction)f2).lhs, eq, boundObjects) 
				&& almostEqual(((Conjunction)f1).rhs,((Conjunction)f2).rhs, eq, boundObjects) ;
		}
		else if(f1 instanceof Disjunction){
			return almostEqual(((Disjunction)f1).lhs,((Disjunction)f2).lhs, eq, boundObjects) 
				&& almostEqual(((Disjunction)f1).rhs,((Disjunction)f2).rhs, eq, boundObjects) ;
		}
		else if(f1 instanceof Implication){
			return almostEqual(((Implication)f1).lhs, ((Implication)f2).lhs, eq, boundObjects) 
				&& almostEqual(((Implication)f1).rhs, ((Implication)f2).rhs, eq, boundObjects) ;
		}
		else if(f1 instanceof Equality){
			Equality eql = (Equality)f1;
			Equality eqr = (Equality)f2;
			boolean lhs = Term.equalOrSub(eql.lhs, eqr.lhs, eq, boundObjects);
			boolean rhs = Term.equalOrSub(eql.rhs, eqr.rhs, eq, boundObjects);
			return lhs && rhs;
		}
		else if(f1 instanceof Negation){
			return almostEqual( ((Negation)f1).formula, ((Negation)f2).formula, eq, boundObjects);
		}
		else if(f1 instanceof QuantifiedFormula){
			QuantifiedFormula quant1 = (QuantifiedFormula) f1;
			QuantifiedFormula quant2 = (QuantifiedFormula) f2;
			ArrayList<LogicObject> newBoundObjectIds = new ArrayList<LogicObject>(boundObjects);
			newBoundObjectIds.add( new LogicObject(quant1.var) );
			return almostEqual( quant1.formula, quant2.formula, eq, newBoundObjectIds);
		}
		else if(f1 instanceof Predicate){
			Predicate p1 = (Predicate) f1;
			Predicate p2 = (Predicate) f2;
			List<Term> p1args = p1.getArgs();
			List<Term> p2args = p2.getArgs();
			
			if(p1args.size() != p2args.size() ) return false;
			for(int i = 0; i < p1args.size(); i++){
				boolean logEq = Term.equalOrSub(p1args.get(i), p2args.get(i), eq, boundObjects);
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
	public static Term[] findTermDifference(Formula formula1, Formula formula2){
		if(formula1.getClass() != formula2.getClass()) {
			//System.err.println(formula1.getClass()+" != "+formula2.getClass());
			return null;
		}
		else if(formula1 instanceof Conjunction){
			Term[] leftResult = findTermDifference(((Conjunction)formula1).lhs, ((Conjunction)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findTermDifference(((Conjunction)formula1).rhs, ((Conjunction)formula2).rhs );
		}
		else if(formula1 instanceof Disjunction){
			Term[] leftResult = findTermDifference(((Disjunction)formula1).lhs, ((Disjunction)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findTermDifference(((Disjunction)formula1).rhs, ((Disjunction)formula2).rhs );
		}
		else if(formula1 instanceof Implication){
			Term[] leftResult = findTermDifference(((Implication)formula1).lhs, ((Implication)formula2).lhs );
			if(leftResult != null) return leftResult;
			return findTermDifference(((Implication)formula1).rhs, ((Implication)formula2).rhs );
		}
		else if(formula1 instanceof Equality){
			Equality eq1 = (Equality) formula1;
			Equality eq2 = (Equality) formula2;
			
			if      (eq1.lhs.equals(eq2.lhs) == false) return new Term[]{eq1.lhs, eq2.lhs};
			else if (eq1.rhs.equals(eq2.rhs) == false) return new Term[]{eq1.rhs, eq2.rhs};
			else return null;
		}
		else if(formula1 instanceof Negation){
			Negation f1 = (Negation) formula1;
			Negation f2 = (Negation) formula2;
			return findTermDifference(f1.formula, f2.formula);
		}
		else if(formula1 instanceof QuantifiedFormula){
			QuantifiedFormula f1 = (QuantifiedFormula) formula1;
			QuantifiedFormula f2 = (QuantifiedFormula) formula2;
			if(f1.type != f2.type) return null;
			else return findTermDifference(f1.formula, f2.formula);
		}
		else if(formula1 instanceof Predicate){
			Predicate f1 = (Predicate) formula1;
			Predicate f2 = (Predicate) formula2;
			if(f1.getArgs().size() != f2.getArgs().size() ) return null;
			for(int i = 0; i < f1.getArgs().size(); i++){
				Term t1 = f1.getArgs().get(i);
				Term t2 = f2.getArgs().get(i);
				if( t1.equals(t2) == false){
					return new Term[]{t1,t2};
				}
			}
			return null;
		}
		else{
			System.err.println("Formula.findObjectIdDifference: non-exhaustive matching for class:"+formula1.getClass().getSimpleName());
		}
		return null;
	}
}

