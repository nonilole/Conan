package model;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.formulas.*;

public class Parser{
    BufferedReader br;
    final boolean verbose = false;

    /**
     * TODO: explain what the method does and what it returns
     * Should not be called from within Parser since it will overwrite this.br
     * @param str
     * @return
     */
    public Formula parse(String str){
    	if(isObjectId(str)) return new FreshVarFormula(str);
    	if(isContradiction(str)) return new Contradiction();
        StringReader strR = new StringReader(str);
        br = new BufferedReader(strR);
        try{
            return parseFormula1();
        }
        //Handle better...
        catch(IOException e){
            System.out.println(e);
        }
        return null;
        
    }
    
    //TODO: update to handle numbered var names for objects
    boolean isObjectId(String str){
    	if(str.length() == 0) return false;
    	str = str.trim();
    	if(str.length() < 1) return false;
    	String subDigits = "₀₁₂₃₄₅₆₇₈₉";
    	if( str.charAt(0) < 'a' || str.charAt(0) > 'z' ) return false;
    	for(int i = 1; i < str.length(); i++){
    		if( subDigits.contains(str.charAt(i)+"") == false) {
    			System.out.println("The char: "+str.charAt(i)+"");
    			return false;
    		}
    	}
    	return true;
    	//return str.length() == 1 && str.charAt(0) >= 'a' && str.charAt(0) <= 'z';
    }
    
    boolean isContradiction(String str) {
    	//TODO kolla igenom strängen så att det enbart finns whitespece och contradiction
    	Pattern p = Pattern.compile("⊥{1}");
    	Matcher m = p.matcher(str);
    	return m.matches();
    }

    /**
     * TODO: explain what the method does and what it returns
     * @return
     * @throws IOException
     */
    Formula parseFormula1() throws IOException{
        if(verbose)
            System.out.format("parseFormula1, %c\n",(char)peek());
        while(true){
            Formula current = parseFormula2();
            int next = next();
            if( (char)next == '→'){
                return new Implication(current, parseFormula1());
            }
            else if ( next == -1 ){
                return current;
            }
            else{
                throw new ParseException("parseFormula1, "+(char)next);
            }
        }
    }

    /**
     * TODO: explain what the method does and what it returns
     * Formula2, F2   := F3 | F2 ∧ F3 | F2 ∨ F3
     * @return
     * @throws IOException
     */
    Formula parseFormula2() throws IOException{
        if(verbose)
            System.out.format("parseFormula2, %c\n",(char)peek());
        Formula prev = parseFormula3();
        char next = (char)peek();

        while(next == '∧' || next == '∨'){
            if(next == '∧')         ignore('∧');
            else/*if(next == '∨')*/ ignore('∨');
            if( next == '∨'){
                Disjunction f = new Disjunction(prev, parseFormula3());
                prev = f;
            }
            else if( next == '∧'){
                Conjunction f = new Conjunction(prev, parseFormula3());
                prev = f;
            }
            next = (char)peek();
        }
        return prev;
    }

    /**
     * TODO: explain what the method does and what it returns
     * TODO: last part: Term = Term, not implemented yet
     * Formula3, F3   := (F) | ∀xF3 | ∃xF3| ¬F3 | Predicate | Term = Term
     * @return
     * @throws IOException
     */
    Formula parseFormula3() throws IOException{
        if(verbose)
            System.out.format("parseFormula3, %c\n",(char)peek());
        char next = (char)peek();
        if( isValidStartOfPredicate(next) ){
            return parsePredicate();
        }
        else if(next == '¬'){
            ignore('¬');
            Negation f = new Negation(parseFormula3());
            return f;
        }
        else if(next == '∀' || next == '∃'){
            return parseQuant();
        }
        else if(next == '('){
            String str = getParenthesizedString();
            
            Formula f = new Parser().parse(str);
            return f;
        }
        else if(isValidStartOfTerm(next)){
        	return parseEquality();
        }
        else{ 
            throw new ParseException("parseFormula3, "+(char)next);
        }
    }

    /**
     * TODO: explain what the method does and what it returns
     * @return
     * @throws IOException
     */
    Formula parseQuant() throws IOException{
        if(verbose)
            System.out.format("parseQuant, %c\n",(char)peek());
        char next = (char)next();
        char quant;
        if     ( next == '∀' || next == '∃'){
        	quant = next;
        }
        else{
            throw new ParseException("parseQuant, "+(char)next);
        }

        //Parse the variable for this quantifier ie the first x in ∀x(P(x))
        //Should probably define function for this to make it more modifiable
        if( isValidStartOfTerm( (char)peek()) == false){
            throw new ParseException("parseQuant, "+(char)next);
        }
        String var = String.valueOf((char)next());
        Formula formula = parseFormula3();
        return new QuantifiedFormula(formula, var, quant);
    }
    
    Formula parseEquality() throws IOException{
    	if(verbose)
            System.out.format("parseEquality, %c\n",(char)peek());
    	
    	Term t1 = parseTerm();
    	ignore('=');
    	Term t2 = parseTerm();
    	return new Equality(t1, t2);
    }
    
    Formula parsePredicate() throws IOException{
        if(verbose)
            System.out.format("parsePredicate, %c\n",(char)peek());
        String id = parsePredicateId();
        List<Term> args;
        if((char)peek() == '('){
            args = parseArgs();
        }
        else {
        	args = new ArrayList<Term>();
        }
        return new Predicate(id, args);
    }
    
    /**
     * Parse a predicate identifier and return it
     * @return
     * @throws IOException
     */
    String parsePredicateId() throws IOException{
        //Should probably verify that characters are valid.
        return ""+(char)next();
    }
    
    /**
     * Check if given char is allowed as start of predicate. 
     * Currently allows: A-Z
     * @param c
     * @return
     */
    boolean isValidStartOfPredicate(char c){
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Parse a comma separated list of args/terms, like (x,y,z)
     * @return
     * @throws IOException
     */
    List<Term> parseArgs() throws IOException{
        if(verbose)
            System.out.format("parseArgs, %c\n",(char)peek());
        List<Term> args = new ArrayList<Term>();        

        if( (char)next() != '('){
            throw new ParseException("parseArgs, "+(char)next());
        }

        args.add( parseTerm() );
        while( (char)peek() == ','){
            ignore(',');
            args.add( parseTerm() );
        }
        ignore( ')' );

        if(args.isEmpty()){
            throw new ParseException("parseArgs, "+(char)next());
        }
        return args;
    }

    /**
     * Parse an identifier for an object 
     * Currently only allows single character, a-z
     * @return
     * @throws IOException
     */
    Term parseTerm() throws IOException{
        if( isValidStartOfTerm( (char)peek()) == false){
            throw new ParseException("parseArg, "+(char)next());
        }
        String id = String.valueOf((char)next());
        
        String subDigits = "₀₁₂₃₄₅₆₇₈₉";
        
        if( (char)peek() == '('){
            List<Term> args = parseArgs();
            return new Function(id,args);
        }
        else if(subDigits.contains( ((char)peek())+"" )){
            id += parseSubDigits()+"";
        }
        return new LogicObject(id);
    }
    
    private String parseSubDigits() throws IOException{
    	String subDigits = "₀₁₂₃₄₅₆₇₈₉";
    	StringBuilder strB = new StringBuilder();
    	while( subDigits.contains( (char)peek() +"")){
    		strB.append( (char)next() );
    	}
    	return strB.toString();
    	
    }

    boolean isValidStartOfTerm(char c) { return c >= 'a' && c <= 'z'; }

    boolean isBinaryOperator(char c){
        return c == '∧' || c == '∨' || c == '→';
    }

    /**
     * Return next char but doesn't remove it from stream
     * Removes leading spaces
     * @return
     * @throws IOException
     */
    int peek() throws IOException{
        removeLeadingSpaces();
        br.mark(1);
        int r = br.read();
        br.reset();
        return r;
    }

    
    /**
     * Return next char, also removes leading spaces
     * @return
     * @throws IOException
     */
    int next() throws IOException{
        removeLeadingSpaces();
        return br.read();
    }

    /**
     * TODO: explain what the method does
     * @throws IOException
     */
    void removeLeadingSpaces() throws IOException{
        while(true){
            br.mark(1);
            int next = br.read();
            //Also check for other whitespace?
            if ((char)next != ' '){
                br.reset();
                return;
            }
        }
    }
    
    /**
     * Read input stream and return string from inside parenthesis. 
     * if next characters in stream are: (123)456 
     * return "123", next chars in stream should now be: 456
     * @return
     * @throws IOException
     */
    String getParenthesizedString() throws IOException{
        if((char)next() != '('){
            throw new ParseException("getParenthesizedString, "+(char)next());
        }
        StringBuilder strB = new StringBuilder();
        int open = 1; //# '(' not yet closed
        while(open > 0){
            int next = next();
            if(next == -1){
                throw new ParseException("Missing right-bracket");
            }
            else if( (char)next == '('){
                strB.append( (char)next );
                open++;
            }
            else if( (char)next == ')'){
                if(open == 1){
                    break;
                }
                strB.append( (char)next );
                open--;
            }
            else{
                strB.append( (char)next );
            }
        }
        return strB.toString();
    }

    /**
     * print the remaining characters in the stream
     * @throws IOException
     */
    void printStream() throws IOException{
        while(true){
            int next = next();
            if(next == -1) break;
            System.out.print((char)next);
        }
        System.out.print("\n");
    }

    /**
     * remove next character in stream and make sure it's the expected one
     * @param c
     * @throws IOException
     */
    void ignore(char c) throws IOException{
    	char next = (char)next();
        if( next != c){
            throw new ParseException("ignore; expected: "+c+", got: "+next);
        }
    }
}










