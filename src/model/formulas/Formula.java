
package model.formulas;

//Use interface instead?
public abstract class Formula {
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
			System.err.println("Formula.findObjectIdDifference: non-exhaustive matching");
		}
		return null;
	}
}

