
package model.formulas;

//Use interface instead?
public abstract class Formula {
	protected int precedence;
	public int getPrecedence(){
		return precedence;
	}
	
	public abstract Formula replace(String newId,String oldId);
}

