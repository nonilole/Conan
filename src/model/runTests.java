package model;

import model.formulas.RandomFormulaGenerator;

public class runTests {

	public static void main(String[] args) {
		System.out.println("Running tests!");
		//equalsTest.run();
		//parserTest.run();
		//new RandomFormulaGenerator().printFormulas(20, 2);
		//VerificationTest.isInScopeRowTest();
		//VerificationTest.isInScopeIntervallTest();
		VerificationTest.boxTest();
		System.out.println("Done.");
	}

}
