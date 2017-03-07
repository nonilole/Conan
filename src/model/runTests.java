package model;

import model.formulas.RandomFormulaGenerator;

public class runTests {

	public static void main(String[] args) {
		//equalsTest.run();
		//parserTest.run();
		new RandomFormulaGenerator().printFormulas(20, 2);
	}

}
