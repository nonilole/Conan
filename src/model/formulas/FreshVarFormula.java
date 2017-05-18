package model.formulas;

public class FreshVarFormula extends Formula {
	
	public final String var;
	
	public FreshVarFormula(String var){ this.var = var; }
	
	@Override
	public Formula replace(String newId, String oldId) {
		throw new RuntimeException("This shouldn't be called");
		//return null;
	}

	@Override
	public boolean containsFreeObjectId(String id) {
		return id.equals(var);
	}
	
	@Override
	public String toString(){
		return var;
	}
	@Override
	public String parenthesize() {
	    return toString();
	}


		@Override
	public Formula replace(Term newId, Term oldId) {
		throw new RuntimeException("This shouldn't be called");
	}

}
