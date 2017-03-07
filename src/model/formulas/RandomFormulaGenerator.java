package model.formulas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFormulaGenerator {
	//weights for the proportion of various types of formulas
	final int EQUALITY_WEIGHT = 10;
	final int PREDICATE_WEIGHT = 20;
	final int NEGATION_WEIGHT = 20;
	final int QUANTIFIED_WEIGHT = 20;
	final int CONJUNCTION_WEIGHT = 20;
	final int DISJUNCTION_WEIGHT = 20;
	final int IMPLICATION_WEIGHT = 20;
	final int FORMULA_WEIGHT_SUM = EQUALITY_WEIGHT + PREDICATE_WEIGHT + NEGATION_WEIGHT + QUANTIFIED_WEIGHT+ 
								   CONJUNCTION_WEIGHT + DISJUNCTION_WEIGHT + IMPLICATION_WEIGHT;
	
	//weights for the proportion of Functions and LogicObjects
	final int LOGICALOBJECT_WEIGHT = 40;
	final int FUNCTION_WEIGHT = 10;
	final int TERM_WEIGHT_SUM = FUNCTION_WEIGHT + LOGICALOBJECT_WEIGHT;
	
	
	
	Random rand = new Random();
	
	public void printFormulas(int nrOfFormulas, int scalingFactor){
		System.out.println("Printing "+nrOfFormulas+" formulas with scaling factor "+scalingFactor);
		for(int i = 0; i < nrOfFormulas; i++){
			System.out.println(generateFormula(scalingFactor));
		}
	}
	
	//generate a random formula, argument x is a way to regulate the length of the formula
	//bigger scalingFactor -> longer formula
	Formula generateFormula(int scalingFactor){
		//scalingFacto should decrease each time to prevent infinitely large formulas
		//rand.nextInt will throw runtime error if given argument < 1
		//System.out.println("scalingFactor: "+scalingFactor);
		if(scalingFactor < 2) {
			scalingFactor = 0;
		}
		else{
			scalingFactor -= 1 + rand.nextInt(scalingFactor/2);
		}
		//System.out.println("new scalingFactor: "+scalingFactor);
		//if x is zero or less we want a predicate or equality, make sure ranInt stays within limit for that
		int ranInt = (scalingFactor > 0) ? rand.nextInt(FORMULA_WEIGHT_SUM) :  rand.nextInt(EQUALITY_WEIGHT + PREDICATE_WEIGHT);
		int acc = 0;
		
		if( ranInt < ( acc += EQUALITY_WEIGHT ) ){
			return new Equality( generateTerm(), generateTerm());
		}
		else if( ranInt < ( acc += PREDICATE_WEIGHT ) ){
			return new Predicate(randomPredicateId() ,generateArgsList());
		}
		else if( ranInt < ( acc += NEGATION_WEIGHT ) ){
			return new Negation( generateFormula(scalingFactor));
		}
		else if( ranInt < ( acc += QUANTIFIED_WEIGHT ) ){
			return new QuantifiedFormula(generateFormula(scalingFactor),generateQuantifiedVariable(), generateQuantifier());
		}
		else if( ranInt < ( acc += CONJUNCTION_WEIGHT ) ){
			return new Conjunction( generateFormula(scalingFactor), generateFormula(scalingFactor));
		}
		else if( ranInt < ( acc += DISJUNCTION_WEIGHT ) ){
			return new Conjunction( generateFormula(scalingFactor), generateFormula(scalingFactor));
		}
		else if( ranInt < ( acc += IMPLICATION_WEIGHT ) ){
			return new Implication( generateFormula(scalingFactor), generateFormula(scalingFactor));
		}
		else{
			throw new RuntimeException("generateFormula: end of else reached");
		}
	}
	
	//Issue: Never generates functions as arguments for functions... 
	Term generateTerm(){
		String randId = randomTermId();
		if(rand.nextInt(TERM_WEIGHT_SUM) < LOGICALOBJECT_WEIGHT){
			return new LogicObject( randId );
		}
		else{
			return new Function(randId, generateArgsList());
		}
	}
	
	List<Term> generateArgsList(){
		//how many arguments
		//usually just one, sometimes want 2 or 3
		int nrArgs;
		int randInt = rand.nextInt(10);
		if( randInt % 2 == 0){
			nrArgs = 1;  //0,2,4,6,8
		}else if(randInt % 3 == 0){
			nrArgs = 2;  //3,9
		}else if(randInt % 5 == 0){
			nrArgs = 3;  //5
		}else{
			nrArgs = 1;  //1,7
		}
		List<Term> args = new ArrayList<Term>();
		for(int i = 0; i < nrArgs ; i++){
			args.add( generateTerm() );
		}
		return args;
	}
	
	String randomPredicateId(){
		//I'm sorry, this  line is a mess. It should return a single character string like "A" or "Z"
		return ""+((char) (rand.nextInt('Z' - 'A' + 1) + 'A'));
	}
	
	String randomTermId(){
		//I'm sorry, this  line is a mess. It should return a single character string like "a" or "z"
		return ""+((char) (rand.nextInt('z' - 'a' + 1) + 'a'));
	}
	
	char generateQuantifier(){
		return rand.nextInt(2) == 0 ? '∀' : '∃';
	}
	
	String generateQuantifiedVariable(){
		return randomTermId();
	}
}
