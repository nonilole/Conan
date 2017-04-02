package model.rules;

public class Premise implements Rule {
	
	
	
	@Override
	public boolean hasCompleteInfo() {
		return true;
	}

	@Override
	public String toString(){
		return "Premise";
	}
}
