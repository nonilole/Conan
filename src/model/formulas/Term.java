
package model.formulas;

//needed for categorizing logical objects and functions as terms
public interface Term{
	public Term replace(String oldId, String newId);
}
