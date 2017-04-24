package model.formulas;

public class Contradiction extends Formula {

	@Override
	public Formula replace(String newId, String oldId) {
		return new Contradiction();
	}

	@Override
	public boolean containsObjectId(String id) {
		return false;
	}
	
	public String toString() {
		return "⊥";
	}
}
