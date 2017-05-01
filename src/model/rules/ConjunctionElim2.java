package model.rules;

import start.Constants;

public class ConjunctionElim2 extends ConjunctionElim {

    public ConjunctionElim2() { super(2); }
    
    @Override
	public String getDisplayName() {
		return Constants.conjunctionElim2;
	}
}
