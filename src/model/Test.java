package model;

import model.formulas.*;

public class Test {
	public static void main(String[] args){
		//formulaReplace();
		Parser parser = new Parser();
		String toParse = "P(x₁) ∧ Q(y)";
		Formula f = parser.parse(toParse);
		System.out.println("Parsing: "+toParse);
		System.out.println("Got:     "+f);
		System.out.println("Which is of class: "+f.getClass().getSimpleName());
	}
	
	private static void formulaReplace(){
		Parser parser = new Parser();
		Formula f1 = parser.parse("P(a) ∧ Q(y) ∧ X(a(b,c,d,e))");
		System.out.println(""+f1);
		System.out.println(f1.replace("x", "a"));
		System.out.println(f1.replace(new LogicObject("x"), new LogicObject("a")));
	}
}
