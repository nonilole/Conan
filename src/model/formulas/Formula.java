
package model.formulas;

//Use interface instead?
public abstract class Formula {
	protected int precedence;
	public int getPrecedence(){
		return precedence;
	}
	
	//return new formula that has replaced, for all free LogicObjects with id=oldId, with newId, 
	public abstract Formula replace(String newId,String oldId);
}

