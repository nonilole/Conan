package model.rules;

import model.Box;
import model.formulas.Formula;

	/*
     * To implement verification of a new rule you need to do the following:
	 * 1. Create the rule object.
	 * 2. Add an else if clause to the Proof.verifyRow function
	 * 3. Implement the method for verification in this class, a prototype method is below this comment.
	 * 4. GUI? Maybe have to add something in the GUI code?
	 */

//Prototype method:
    /*
	//Author
	static boolean verifyRuleName(ProofData data, int rowToVerify ){
		// Stuff to check:
		// is the rule object of the correct type? Probably just check with an assertion
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
			// Box.isInScope should check both scope and if the data is verified
		// does the referenced rows contain the data for this rule to be correct?
	}
	*/

public abstract class Rule {

    //if this is false there is no reason to continue the verification
    //since info is missing
    public abstract boolean hasCompleteInfo();

    //Update the reference in the rule object
    public abstract void updateReference(int index, String newValue);

    public abstract boolean verifyReferences(Box data, int rowIndex);

    public abstract boolean verifyRow(Box data, int rowIndex);

    public abstract Formula generateRow(Box data);

    public Formula generateFormula(Box data, int rowIndex) {
        if (verifyReferences(data, rowIndex))
            return generateRow(data);
        return null;
    }

    public boolean verify(Box data, int rowIndex) {
        return (verifyReferences(data, rowIndex) && verifyRow(data, rowIndex));
    }
}
