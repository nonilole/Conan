package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.formulas.Formula;

public class equalsTest {

	//main method only for testing
    public static void run(){
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
