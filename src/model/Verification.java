package model;

import model.formulas.*;
import model.rules.*;

class Verification {
	
	//Prototype method:
	/*
	//Author
	static boolean verifyRuleName(ProofData data, int rowToVerify ){
		// Stuff to check:
		// is the rule object of the correct type? Probably just check with an assertion
		// are the references in the rule object in scope of rowIndex?
		// are all the referenced rows verified?
			// ProofData.isInScope should check both scope and if the data is verified
		// do we have the needed references to make the deduction?
	}
	*/
	
	//Andreas
	//Not done/correct! Some of this code will laso be moved elsewhere
	static boolean verifyAndIntro(Box data, int rowIndex){
		//System.out.println("Verification.verifyAndIntro");
		try{
			// is the rule object of the correct type?
			ProofRow rowToVerify = data.getRow( rowIndex );
			assert(rowToVerify.getRule() instanceof ConjunctionIntroRule):"Incorrect rule type in Verification.verifyAndIntro";
			ConjunctionIntroRule rule = (ConjunctionIntroRule) rowToVerify.getRule();
			
			// are the references in the rule object in scope of rowIndex?
			// are all the referenced rows verified?
			// ProofData.isInScope should check scope and if the data is verified
			if( data.isInScopeOf( rule.getPremise1()-1, rowIndex) == false ||
				data.isInScopeOf( rule.getPremise2()-1, rowIndex) == false )
			{
				System.out.println("    Scope issue");
				return false;
			}
			
			// do we have the needed premises/references to make the deduction?
			if(rowToVerify.getFormula() instanceof Conjunction == false) return false;
			Conjunction conclusion = (Conjunction)rowToVerify.getFormula();
			Formula ref1 = data.getRow( rule.getPremise1()-1 ).getFormula();
			Formula ref2 = data.getRow( rule.getPremise2()-1 ).getFormula();
			return verifyAndIntro(ref1,ref2, conclusion);
		}
		catch(IndexOutOfBoundsException e){
			//should provide user with some  info maybe?
			System.out.println("verifyAndIntro: "+e);
			return false;
		}
	}
	
	static boolean verifyAndIntro(Formula premise1, Formula premise2, Conjunction result){
		return result.equals(new Conjunction(premise1, premise2));
	}
	
	//Andreas
	//Not done/correct! Some of this code will laso be moved elsewhere
	static boolean verifyImplicationIntro(Box data, int rowIndex){
		System.out.println("Verification.verifyImplicationIntro");
		try{
			// is the rule object of the correct type?
			ProofRow rowToVerify = data.getRow( rowIndex );
			assert(rowToVerify.getRule() instanceof ImplicationIntroRule) : 
				"Incorrect rule type function: Verification.verifyImplicationIntro";
			ImplicationIntroRule rule = (ImplicationIntroRule) rowToVerify.getRule();
			
			System.out.println("scope issue");
			// are the references in the rule object in scope of rowIndex?
			// are all the referenced rows verified?
			// ProofData.isInScope should check both of these
			Intervall premiseIntervall = rule.getPremiseIntervall();
			if( data.isInScopeOf(premiseIntervall, rowIndex) == false) return false;
			System.out.println("not scope issue");
			
			//do we have the needed premises/references to make the deduction?
			if(rowToVerify.getFormula() instanceof Implication == false) return false;
			Implication conclusion = (Implication)rowToVerify.getFormula();
			Formula assumption      = data.getRow( premiseIntervall.startIndex ).getFormula();
			Formula conclusionOfBox = data.getRow( premiseIntervall.endIndex ).getFormula();
			return verifyImplicationIntro(assumption, conclusionOfBox, conclusion);
			
		}
		catch(IndexOutOfBoundsException e){
			//should provide user with some  info maybe?
			System.out.println("verifyImplicationIntro: "+e);
			return false;
		}
	}
	
	static boolean verifyImplicationIntro(Formula assumption, Formula conclusionOfBox, Implication conclusion){
		return conclusion.equals(new Implication(assumption, conclusionOfBox));
	}
}
