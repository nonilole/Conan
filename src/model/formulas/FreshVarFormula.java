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
	public boolean containsObjectId(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
