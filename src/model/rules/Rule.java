package model.rules;

public abstract class Rule { // should probably be an interface instead
	
	//if this is false there is no reason to continue the verification
	//since info is missing
	public abstract boolean hasCompleteInfo();
}
