package model;

import java.io.File;
import java.util.Scanner;

import model.formulas.Formula;

public class parserTest {
	
	//main method only for testing
    public static void run(){
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
}
