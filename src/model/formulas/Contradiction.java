package model.formulas;

public class Contradiction extends Formula {

	@Override
	public Formula replace(String newId, String oldId) {
		return new Contradiction();
	}
	
	@Override
	public Formula replace(Term newT, Term oldT) {
		return new Contradiction();
	}

	@Override
	public boolean containsFreeObjectId(String id) {
		return false;
	}
	
	public String toString() {
		return "⊥";
	}
	public String parenthesize() {
		return "⊥";
	}

	@Override
	public boolean equals(Object o){
		return o instanceof Contradiction;
	}
}
