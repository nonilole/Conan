package model.rules;

import start.Constants;

public class ConjunctionElim1 extends ConjunctionElim {
    public ConjunctionElim1() {
        super(1);
    }
    
    @Override
	public String getDisplayName() {
		return Constants.conjunctionElim1;
	}
}
