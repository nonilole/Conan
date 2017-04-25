package model;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.ProofListener.RowInfo;
import model.formulas.Equality;
import model.formulas.Formula;
import model.formulas.LogicObject;
import model.formulas.QuantifiedFormula;
import model.formulas.RandomFormulaGenerator;
import model.rules.*;

//public class Tests {
//	public static void main(String[] args) {
//		System.out.println("Running tests!");
//		//equalsTest();
//		//parserTest();
//		//new RandomFormulaGenerator().printFormulas(20, 2);
//		//isInScopeRowTest();
//		//isInScopeIntervalTest();
//		//getBoxTest();
//		//testContradictionElim();
//		//verificationTest();
//		//replaceVarTest(20);
//		//replaceVarTest2();
//		//existElimTest();
//		//printRandomFormulas(20);
//		//testEqualOrSub();
//		//isInstantiationOfTest(10000);
//		getProofInfoTest();
//		System.out.println("Done.");
//	}
//
//	public static void getProofInfoTest(){
//		Proof proof = new Proof();
//		proof.addRow();//1
//		proof.openBox();
//		proof.addRow();//2
//		proof.updateFormulaRow("∃xP(x)", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("y", 2);
//		proof.addRule(2, new FreshVar());
//		proof.addRow();//3
//		proof.updateFormulaRow("P(y)", 3);
//		proof.addRule(3, new Premise());
//		proof.addRow();//4
//		proof.updateFormulaRow("Q", 4);
//		proof.addRule(4, new Premise());
//		proof.closeBox();
//		proof.addRow();
//		proof.updateFormulaRow("Q", 5);
//		proof.addRule(5, new ExistsElim());
//		proof.rulePromptUpdate(5, 1, "1");
//		proof.rulePromptUpdate(5, 2, "2-4");
//		System.out.println("\n==================================================================\n");
//		List<RowInfo> list = proof.getProofInfo();
//		for(RowInfo row : list){ System.out.println(row+"");}
//
//	}
//
//	public static void testEqualOrSub(){
//		Parser parser = new Parser();
//		Equality xeqy = (Equality)parser.parse("g=z");
//
//		//Try different formulas for f1 and f2
//		Formula f1 = parser.parse("∀xI(b(g),x)");
//		Formula f2 = parser.parse("∀xI(b(z),x)");
//
//		boolean result = Formula.almostEqual(f1, f2, xeqy, new ArrayList<String>());
//		System.out.println(result);
//
//	}
//
//	public static void isInstantiationOfTest(int nrTests){
//		RandomFormulaGenerator rangen = new RandomFormulaGenerator();
//		Random rand = new Random();
//		final int idVariety = 3;
//		final boolean printAll = false;
//
//		for(int i = 0; i < nrTests; i++){
//			//System.out.println("Test: "+(i+1));
//			Formula ranf = rangen.generateFormula(5, idVariety);
//			char chr = (char) (rand.nextInt(idVariety)+'a');
//			char chr2 = (char) (rand.nextInt(idVariety)+'a');
//			//create quantified formula  from generated formula
//			QuantifiedFormula quant = new QuantifiedFormula(ranf,chr+"",'∀');
//			ranf = ranf.replace(chr2+"", chr+"");
//
//			if(printAll || Formula.isInstantiationOf(ranf, quant) == false){
//				System.out.println("isInstantiationOf:");
//				System.out.println(""+ranf);
//				System.out.println(""+quant);
//				System.out.println("isInstantiationOf: "+Formula.isInstantiationOf(ranf, quant));
//				System.out.println("=====================================");
//			}
//		}
//		//String blö = res[2];
//	}
//
//	public static void existElimTest(){
//		Proof proof = new Proof();
//		proof.addRow();//1
//		proof.openBox();
//		proof.addRow();//2
//		proof.updateFormulaRow("∃xP(x)", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("y", 2);
//		proof.addRule(2, new FreshVar());
//		proof.addRow();//3
//		proof.updateFormulaRow("P(y)", 3);
//		proof.addRule(3, new Premise());
//		proof.addRow();//4
//		proof.updateFormulaRow("Q", 4);
//		proof.addRule(4, new Premise());
//		proof.closeBox();
//		proof.addRow();
//		proof.updateFormulaRow("Q", 5);
//		proof.addRule(5, new ExistsElim());
//		proof.rulePromptUpdate(5, 1, "1");
//		proof.rulePromptUpdate(5, 2, "2-4");
//		proof.printProof(true);
//	}
//
//	public static void printRandomFormulas(int nrOfFormulas){
//		RandomFormulaGenerator gen = new RandomFormulaGenerator();
//		for(int i = 0; i<nrOfFormulas; i++){
//			System.out.println(""+gen.generateFormula(5));
//		}
//	}
//
//	public static void replaceVarTest2(){
//		//∀ ∃ →
//		Formula parsed = new Parser().parse("P(x) ∧ ∀x(P(x) → Q(x))");
//		System.out.println("Parsed:\n"+parsed);
//		System.out.println("Replaced:\n"+parsed.replace("b", "x"));
//		System.out.println("Parsed again:\n"+parsed);
//	}
//
//	public static void replaceVarTest(int nrTests){
//		RandomFormulaGenerator gen = new RandomFormulaGenerator();
//		Random rand = new Random();
//		for(int i = 0; i < nrTests; i++){
//			int nrVarId = 2;
//			Formula randFormula = gen.generateFormula(5,nrVarId);
//			char oldChar = (char)(rand.nextInt(nrVarId+1)+'a');
//			char newChar = (char)(rand.nextInt(nrVarId+1)+'a');
//			System.out.println(oldChar+"->"+newChar+" | "+randFormula);
//			System.out.println("     | "+randFormula.replace(newChar+"", oldChar+""));
//			System.out.println("===============================================");
//		}
//	}
//
//	public static void testDisjunctionIntro() {
//		Proof proof = new Proof();
//		//DisjunctionIntro
//		System.out.println("DisjunctionIntro");
//		proof.addRow();//1
//		proof.addRow();//2
//		proof.updateFormulaRow("A", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("A ∨ B", 2);
//		proof.addRule(2, new DisjunctionIntro(0));
//		proof.printProof(true);
//	}
//	public static void testConjunctionElim() {
//		Proof proof = new Proof();
//		//ConjunctionElimRule test
//		System.out.println("ConjunctionElim");
//		proof = new Proof();
//		proof.addRow();//1
//		proof.openBox();
//		proof.addRow();//2
//		proof.closeBox();
//		proof.addRow();//3
//		proof.updateFormulaRow("A ∧ B", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("A ∧ B", 2);
//		proof.addRule(2, new Premise());
//		proof.updateFormulaRow("B", 3);
//		proof.addRule(3, new ConjunctionElim(2, 0));
//		proof.printProof(true);
//
//	}
//	public static void testImplicationIntro() {
//		Proof proof = new Proof();
//		//ConjunctionElimRule test
//		System.out.println("ImplicationIntro");
//		//ImplicationIntroRule test
//		proof.addRow();//1
//		proof.openBox();
//		proof.addRow();//2
//		proof.addRow();//3
//		proof.closeBox();
//		proof.addRow();//4
//		proof.updateFormulaRow("X", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("A", 2);
//		proof.addRule(2, new Premise());
//		proof.updateFormulaRow("B", 3);
//		proof.addRule(3, new Premise());
//		proof.updateFormulaRow("A → B", 4);
//		proof.addRule(4, new ImplicationIntro(new Interval(1,2)));
//		proof.printProof(true);
//	}
//
//
//	public static void testContradictionElim() {
//		Proof proof = new Proof();
//		System.out.println("ContradictionElim");
//		proof.addRow();
//		proof.updateFormulaRow("⊥", 1);
//		proof.addRule(1,new Premise());
//		proof.addRow();
//		proof.updateFormulaRow("(P ∧ Q) → (Q ∨ R) ", 2);
//		proof.addRule(2,new ContradictionElim(0));
//		proof.printProof(true);
//	}
//
//	public static void testEqualityIntro() {
//		Proof proof = new Proof();
//		proof.addRow();
//		proof.updateFormulaRow("x = x", 1);
//		proof.addRule(1, new EqualityIntro());
//		proof.printProof(true);
//	}
//
//	public static void testDoubleNegationElim() {
//		Proof proof = new Proof();
//		proof.addRow();
//		proof.addRow();
//		proof.updateFormulaRow("¬¬X", 1);
//		proof.addRule(1, new Premise());
//		proof.updateFormulaRow("X", 2);
//		proof.addRule(2, new DoubleNegationElim(0));
//		proof.printProof(true);
//	}
//
//	public static void verificationTest(){
//	    testConjunctionElim();
//	    testDisjunctionIntro();
//	    testImplicationIntro();
//	    testEqualityIntro();
//	    testDoubleNegationElim();
//	}
//
//	public static void isInScopeIntervalTest(){
//		Proof proof = new Proof();
//		proof.addRow();//0
//		proof.openBox();
//		proof.addRow();//1
//		proof.addRow();//2
//		proof.closeBox();
//		proof.addRow();//3
//		proof.openBox();
//		proof.addRow();//4
//		proof.openBox();
//		proof.addRow();//5
//		proof.closeBox();
//		proof.openBox();
//		proof.addRow();//6
//		proof.addRow();//7
//		proof.closeBox();
//		proof.addRow();//8
//		proof.addRow();//9
//		proof.closeBox();
//		proof.addRow();//10
//
//		proof.printProof(true);
//		proof.printBoxes();
//		//Box data = proof.getData();
//		//System.out.println("isInscopeOf( (1-2), 3) :"+data.isInScopeOf(new Interval(1,2), 3));
//		proof.printIntervalScopes(true);
//	}
//
//	public static void isInScopeRowTest(){
//		Proof proof = new Proof();
//		proof.addRow();//0
//		proof.openBox();
//		proof.addRow();//1
//		proof.addRow();//2
//		proof.closeBox();
//		proof.addRow();//3
//		proof.openBox();
//		proof.addRow();//4
//		proof.openBox();
//		proof.addRow();//5
//		proof.closeBox();
//		proof.openBox();
//		proof.addRow();//6
//		proof.addRow();//7
//		proof.closeBox();
//		proof.addRow();//8
//		proof.addRow();//9
//
//		proof.printProof(true);
//		proof.printBoxes();
//		proof.printRowScopes(true);
//	}
//
//	public static void getBoxTest(){
//		Proof proof = new Proof();
//		Box data = proof.getData();
//		proof.addRow();//0
//		proof.openBox();
//		proof.addRow();//1
//		proof.addRow();//2
//		proof.openBox();
//		proof.addRow();//3
//		proof.openBox();
//		proof.addRow();//4
//		proof.closeBox();
//		proof.addRow();//5
//		proof.closeBox();
//		proof.openBox();
//		proof.addRow();//6
//		proof.addRow();//7
//		proof.closeBox();
//		proof.addRow();//8
//		proof.addRow();//9
//
//		proof.printProof(true); //zeroBasedNumbering?
//		proof.printBoxes();
//	}
//
//	public static void parserTest(){
//        Parser pr = new Parser();
//        try{
//            Scanner scr = new Scanner(new File("resources/wellFormedFormulas.txt"));
//            System.out.println("\nParsing well formed formulas:");
//            System.out.println("=====================================================");
//            int success = 0;
//            int fail = 0;
//            while(scr.hasNext()){
//                String toParse = scr.nextLine();
//                if(toParse.startsWith("//") || toParse.equals(""))
//                    continue;
//                //System.out.println("Input:   "+toParse);
//                Formula f;
//                try{
//                	f = pr.parse(toParse);
//                	success++;
//                	//System.out.println("Parsed!");
//                }catch(ParseException e){
//                	fail++;
//                	System.out.println("Input:"+ toParse);
//                	System.out.println("Error: " + e.getMessage());
//                	System.out.println("=====================================================");
//                	continue;
//                }
//            }
//            scr.close();
//            //System.out.println("=====================================================");
//            System.out.println("Done with wellformed formulas, successfully parsed "+success+" formulas");
//            System.out.println("Failed to parse "+fail+" formulas");
//            System.out.println("=====================================================");
//
//
//            fail = 0;
//            success = 0;
//            scr = new Scanner(new File("resources/notWellformedFormulas.txt"));
//            System.out.println("\n\nParsing not wellformed formulas:");
//            System.out.println("=====================================================");
//            while(scr.hasNext()){
//                String toParse = scr.nextLine();
//                if(toParse.startsWith("//") || toParse.equals(""))
//                    continue;
//                //System.out.println("Input:   "+toParse);
//                Formula f;
//                try{
//                	f = pr.parse(toParse);
//                	fail++;
//                	System.out.println("Input:   "+toParse);
//                	System.out.println("Failed to catch misformed formula!");
//                }catch(ParseException e){
//                	//System.out.println("Error: " + e.getMessage());
//                	//System.out.println("=====================================================");
//                	success++;
//                	continue;
//                }
//            }
//            System.out.println("=====================================================");
//            System.out.println("Done with not wellformed formulas, successfully caught "+success+" misformed formulas");
//            System.out.println("Failed to catch "+fail+" misformed formulas");
//            //System.out.println("=====================================================");
//        }
//        catch(Exception e){
//            System.out.println("Tests failed");
//            System.out.println(e);
//        }
//    }
//
//	public static void equalsTest(){
//    	final int NROFTESTS = 100;
//        Parser pr = new Parser();
//        List<Formula> formulas = new ArrayList<Formula>();
//        List<TestTuple> equal = new ArrayList<TestTuple>();
//        List<TestTuple> notEqual = new ArrayList<TestTuple>();
//        List<TestTuple> nonCommuting = new ArrayList<TestTuple>();
//        try{
//            Scanner scr = new Scanner(new File("resources/wellFormedFormulas.txt"));
//            while(scr.hasNext()){
//                String toParse = scr.nextLine();
//                if(toParse.startsWith("//") || toParse.equals(""))
//                    continue;
//
//                //System.out.println("Input:   "+toParse);
//
//                Formula f;
//                try{
//                	formulas.add(pr.parse(toParse));
//                }catch(ParseException e){
//                	System.out.println("ParseError on string: "+toParse);
//                	continue;
//                }
//            }
//            scr.close();
//
//            Random rand = new Random();
//            for(int i = 0; i < NROFTESTS ; i++){
//            	int bound = formulas.size();
//            	Formula f1 = formulas.get(rand.nextInt(bound));
//            	Formula f2 = formulas.get(rand.nextInt(bound));
//            	if(f1.equals(f2)){
//            		if(f2.equals(f1)){
//            			equal.add( new TestTuple(f1,f2));
//            		}
//            	}else if(f2.equals(f1)){
//            		nonCommuting.add(  new TestTuple(f1,f2));
//            	}else{
//            		notEqual.add(new TestTuple(f1,f2));
//            	}
//            }
//
//            for(TestTuple tt : notEqual){
//            	System.out.println("Not equal: "+ tt.f1);
//            	System.out.println("and        "+ tt.f2);
//            	System.out.println("============================================================");
//            }
//            for(TestTuple tt : equal){
//            	System.out.println("Equal: "+ tt.f1);
//            	System.out.println("and    "+ tt.f2);
//            	System.out.println("============================================================");
//            }
//            for(TestTuple tt : nonCommuting){
//            	System.out.println("Not commuting: "+ tt.f1);
//            	System.out.println("and            "+ tt.f2);
//            	System.out.println("============================================================");
//            }
//
//
//        }
//        catch(Exception e){
//            System.out.println("Tests failed");
//            System.out.println(e);
//        }
//    }
//
//    static class TestTuple{
//    	public Formula f1;
//    	public Formula f2;
//    	public TestTuple(Formula f1, Formula f2){
//    		this.f1 = f1;
//    		this.f2 = f2;
//    	}
//    }
//}

	
