package model;

import model.formulas.*;
import model.rules.*;

class Verification {
	
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
	
	//Andreas
	static boolean verifyConjunctionIntro(Box data, int rowIndex){
		//System.out.println("Verification.verifyAndIntro");
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );
		assert(rowToVerify.getRule() instanceof ConjunctionIntroRule):"Incorrect rule type in Verification.verifyConjunctionIntro";
		ConjunctionIntroRule rule = (ConjunctionIntroRule) rowToVerify.getRule();
			
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
			// ProofData.isInScope should check scope and if the data is verified
		if( data.isInScopeOf( rule.getPremise1()-1, rowIndex) == false || //this currently assumes the premise is stored as a rowNr rather than index
			data.isInScopeOf( rule.getPremise2()-1, rowIndex) == false )
		{
			System.out.println("    Scope issue");
			return false;
		}
			
		// do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Conjunction == false) return false;
		Conjunction conclusion = (Conjunction)rowToVerify.getFormula();
		Formula ref1 = data.getRow( rule.getPremise1()-1 ).getFormula();//this currently assumes the premise is stored as a rowNr rather than index
		Formula ref2 = data.getRow( rule.getPremise2()-1 ).getFormula();
		return verifyConjunctionIntro(ref1,ref2, conclusion);
	}
	
	//it's nice to break this out for potential testing maybe?
	static boolean verifyConjunctionIntro(Formula premise1, Formula premise2, Conjunction result){
		return result.equals(new Conjunction(premise1, premise2));
	}
	
	//Andreas
	static boolean verifyConjunctionElim(Box data, int rowIndex){
	    // is the rule object of the correct type? Probably just check with an assertion
	    ProofRow rowToVerify = data.getRow( rowIndex );
        assert(rowToVerify.getRule() instanceof ConjunctionElimRule):"Incorrect rule type in Verification.verifyConjunctionIntro";
        ConjunctionElimRule rule = (ConjunctionElimRule) rowToVerify.getRule();
        
        // are the references in the rule object in scope of rowIndex?        
        // are all the referenced rows verified?
          // Box.isInScope should check both scope and if the data is verified
        if( data.isInScopeOf( rule.getPremise(), rowIndex ) == false ) return false;
        
        // do we have the needed references to make the deduction?
        Formula result = data.getRow(rowIndex).getFormula();
        Formula reference = data.getRow( rule.getPremise() ).getFormula();
        if( reference instanceof Conjunction == false) return false;
        Conjunction ref = (Conjunction)reference;
        if( rule.getType() == 1){
            return result.equals(ref.lhs);
        }
        else{ //rule.getType() == 2
          return result.equals(ref.rhs);
        }
	}
	
	//Andreas
	static boolean verifyImplicationIntro(Box data, int rowIndex){
		//System.out.println("Verification.verifyImplicationIntro");
		
		// is the rule object of the correct type?
		ProofRow rowToVerify = data.getRow( rowIndex );
		assert(rowToVerify.getRule() instanceof ImplicationIntroRule) : 
			"Incorrect rule type function: Verification.verifyImplicationIntro";
		ImplicationIntroRule rule = (ImplicationIntroRule) rowToVerify.getRule();
		
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
		// ProofData.isInScope should check both of these
		Intervall premiseIntervall = rule.getPremiseIntervall();
		if( data.isInScopeOf(premiseIntervall, rowIndex) == false) return false;
			
		//do we have the needed premises/references to make the deduction?
		if(rowToVerify.getFormula() instanceof Implication == false) return false;
		Implication conclusion = (Implication)rowToVerify.getFormula();
		Formula assumption      = data.getRow( premiseIntervall.startIndex ).getFormula();
		Formula conclusionOfBox = data.getRow( premiseIntervall.endIndex ).getFormula();
		return verifyImplicationIntro(assumption, conclusionOfBox, conclusion);
			
	}
	
	//it's nice to break this out for potential testing maybe?
	static boolean verifyImplicationIntro(Formula assumption, Formula conclusionOfBox, Implication conclusion){
		return conclusion.equals(new Implication(assumption, conclusionOfBox));
	}
}
