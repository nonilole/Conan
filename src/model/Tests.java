package model;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.formulas.Formula;
import model.formulas.RandomFormulaGenerator;
import model.rules.*;

public class Tests {
	public static void main(String[] args) {
		System.out.println("Running tests!");
		//equalsTest();
		//parserTest();
		//new RandomFormulaGenerator().printFormulas(20, 2);
		//isInScopeRowTest();
		//isInScopeIntervallTest();
		//getBoxTest();
		verificationTest();
		System.out.println("Done.");
	}

	public static void testDisjunctionIntro() {
		Proof proof = new Proof();
		//DisjunctionIntro
		System.out.println("DisjunctionIntro");
		proof.addRow();//1
		proof.addRow();//2
		proof.updateFormulaRow("A", 1);
		proof.addRule(1, new Premise());
		proof.updateFormulaRow("A ∨ B", 2);
		proof.addRule(2, new DisjunctionIntro(0));
		proof.printProof(true);
	}
	public static void testConjunctionElim() {
		Proof proof = new Proof();
		//ConjunctionElimRule test
		System.out.println("ConjunctionElim");
		proof = new Proof();
		proof.addRow();//1
		proof.openBox();
		proof.addRow();//2
		proof.closeBox();
		proof.addRow();//3
		proof.updateFormulaRow("A ∧ B", 1);
		proof.addRule(1, new Premise());
		proof.updateFormulaRow("A ∧ B", 2);
		proof.addRule(2, new Premise());
		proof.updateFormulaRow("B", 3);
		proof.addRule(3, new ConjunctionElim(2, 0));
		proof.printProof(true);

	}
	public static void testImplicationIntro() {
		Proof proof = new Proof();
		//ConjunctionElimRule test
		System.out.println("ImplicationIntro");
		//ImplicationIntroRule test
		proof.addRow();//1
		proof.openBox();
		proof.addRow();//2
		proof.addRow();//3
		proof.closeBox();
		proof.addRow();//4
		proof.updateFormulaRow("X", 1);
		proof.addRule(1, new Premise());
		proof.updateFormulaRow("A", 2);
		proof.addRule(2, new Premise());
		proof.updateFormulaRow("B", 3);
		proof.addRule(3, new Premise());
		proof.updateFormulaRow("A → B", 4);
		proof.addRule(4, new ImplicationIntro(new Intervall(1,2)));
		proof.printProof(true);
	}

	public static void testEqualityIntro() {
		Proof proof = new Proof();
		proof.addRow();
		proof.updateFormulaRow("x = x", 1);
		proof.addRule(1, new EqualityIntro());
		proof.printProof(true);
	}

	public static void testDoubleNegationElim() {
		Proof proof = new Proof();
		proof.addRow();
		proof.addRow();
		proof.updateFormulaRow("¬¬X", 1);
		proof.addRule(1, new Premise());
		proof.updateFormulaRow("X", 2);
		proof.addRule(2, new DoubleNegationElim(0));
		proof.printProof(true);
	}
	
	public static void verificationTest(){
	    testConjunctionElim();
	    testDisjunctionIntro();
	    testImplicationIntro();
	    testEqualityIntro();
	    testDoubleNegationElim();
	}
	
	public static void isInScopeIntervallTest(){
		Proof proof = new Proof();
		proof.addRow();//0
		proof.openBox();
		proof.addRow();//1
		proof.addRow();//2
		proof.closeBox();
		proof.addRow();//3
		proof.openBox();
		proof.addRow();//4
		proof.openBox();
		proof.addRow();//5
		proof.closeBox();
		proof.openBox();
		proof.addRow();//6
		proof.addRow();//7
		proof.closeBox();
		proof.addRow();//8
		proof.addRow();//9
		proof.closeBox();
		proof.addRow();//10
		
		proof.printProof(true);
		proof.printBoxes();
		//Box data = proof.getData();
		//System.out.println("isInscopeOf( (1-2), 3) :"+data.isInScopeOf(new Intervall(1,2), 3));
		proof.printIntervallScopes(true);
	}
	
	public static void isInScopeRowTest(){
		Proof proof = new Proof();
		proof.addRow();//0
		proof.openBox();
		proof.addRow();//1
		proof.addRow();//2
		proof.closeBox();
		proof.addRow();//3
		proof.openBox();
		proof.addRow();//4
		proof.openBox();
		proof.addRow();//5
		proof.closeBox();
		proof.openBox();
		proof.addRow();//6
		proof.addRow();//7
		proof.closeBox();
		proof.addRow();//8
		proof.addRow();//9
		
		proof.printProof(true);
		proof.printBoxes();
		proof.printRowScopes(true);
	}
	
	public static void getBoxTest(){
		Proof proof = new Proof();
		Box data = proof.getData();
		proof.addRow();//0
		proof.openBox();
		proof.addRow();//1
		proof.addRow();//2
		proof.openBox();
		proof.addRow();//3
		proof.openBox();
		proof.addRow();//4
		proof.closeBox();
		proof.addRow();//5
		proof.closeBox();
		proof.openBox();
		proof.addRow();//6
		proof.addRow();//7
		proof.closeBox();
		proof.addRow();//8
		proof.addRow();//9
		
		proof.printProof(true); //zeroBasedNumbering?
		proof.printBoxes();
	}
	
	public static void parserTest(){
        Parser pr = new Parser();
        try{
            Scanner scr = new Scanner(new File("resources/wellFormedFormulas.txt"));
            System.out.println("\nParsing well formed formulas:");
            System.out.println("=====================================================");
            int success = 0;
            int fail = 0;
            while(scr.hasNext()){
                String toParse = scr.nextLine();
                if(toParse.startsWith("//") || toParse.equals(""))
                    continue;
                //System.out.println("Input:   "+toParse);
                Formula f;
                try{
                	f = pr.parse(toParse);
                	success++;
                	//System.out.println("Parsed!");
                }catch(ParseException e){
                	fail++;
                	System.out.println("Input:"+ toParse);
                	System.out.println("Error: " + e.getMessage());
                	System.out.println("=====================================================");
                	continue;
                }
            }
            scr.close();
            //System.out.println("=====================================================");
            System.out.println("Done with wellformed formulas, successfully parsed "+success+" formulas");
            System.out.println("Failed to parse "+fail+" formulas");
            System.out.println("=====================================================");
            
            
            fail = 0;
            success = 0;
            scr = new Scanner(new File("resources/notWellformedFormulas.txt"));
            System.out.println("\n\nParsing not wellformed formulas:");
            System.out.println("=====================================================");
            while(scr.hasNext()){
                String toParse = scr.nextLine();
                if(toParse.startsWith("//") || toParse.equals(""))
                    continue;
                //System.out.println("Input:   "+toParse);
                Formula f;
                try{
                	f = pr.parse(toParse);
                	fail++;
                	System.out.println("Input:   "+toParse);
                	System.out.println("Failed to catch misformed formula!");
                }catch(ParseException e){
                	//System.out.println("Error: " + e.getMessage());
                	//System.out.println("=====================================================");
                	success++;
                	continue;
                }
            }
            System.out.println("=====================================================");
            System.out.println("Done with not wellformed formulas, successfully caught "+success+" misformed formulas");
            System.out.println("Failed to catch "+fail+" misformed formulas");
            //System.out.println("=====================================================");
        }
        catch(Exception e){
            System.out.println("Tests failed");
            System.out.println(e);
        }
    }

	public static void equalsTest(){
    	final int NROFTESTS = 100;
        Parser pr = new Parser();
        List<Formula> formulas = new ArrayList<Formula>();
        List<TestTuple> equal = new ArrayList<TestTuple>();
        List<TestTuple> notEqual = new ArrayList<TestTuple>();
        List<TestTuple> nonCommuting = new ArrayList<TestTuple>();
        try{
            Scanner scr = new Scanner(new File("resources/wellFormedFormulas.txt"));
            while(scr.hasNext()){
                String toParse = scr.nextLine();
                if(toParse.startsWith("//") || toParse.equals(""))
                    continue;
                
                //System.out.println("Input:   "+toParse);
                
                Formula f;
                try{
                	formulas.add(pr.parse(toParse));
                }catch(ParseException e){
                	System.out.println("ParseError on string: "+toParse);
                	continue;
                }
            }
            scr.close();
            
            Random rand = new Random();
            for(int i = 0; i < NROFTESTS ; i++){
            	int bound = formulas.size();
            	Formula f1 = formulas.get(rand.nextInt(bound));
            	Formula f2 = formulas.get(rand.nextInt(bound));
            	if(f1.equals(f2)){
            		if(f2.equals(f1)){
            			equal.add( new TestTuple(f1,f2));
            		}
            	}else if(f2.equals(f1)){
            		nonCommuting.add(  new TestTuple(f1,f2));
            	}else{
            		notEqual.add(new TestTuple(f1,f2));
            	}
            }
            
            for(TestTuple tt : notEqual){
            	System.out.println("Not equal: "+ tt.f1);
            	System.out.println("and        "+ tt.f2);
            	System.out.println("============================================================");
            }
            for(TestTuple tt : equal){
            	System.out.println("Equal: "+ tt.f1);
            	System.out.println("and    "+ tt.f2);
            	System.out.println("============================================================");
            }
            for(TestTuple tt : nonCommuting){
            	System.out.println("Not commuting: "+ tt.f1);
            	System.out.println("and            "+ tt.f2);
            	System.out.println("============================================================");
            }
            
            
        }
        catch(Exception e){
            System.out.println("Tests failed");
            System.out.println(e);
        }
    }
    
    static class TestTuple{
    	public Formula f1; 
    	public Formula f2;
    	public TestTuple(Formula f1, Formula f2){
    		this.f1 = f1;
    		this.f2 = f2;
    	}
    }
}

	
