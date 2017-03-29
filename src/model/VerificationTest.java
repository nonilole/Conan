package model;

import model.rules.*;

class VerificationTest {
	
	public void testAndIntro(){}
	public void testAndElim(){}
	
	
	public static void isInScopeRowTest(){
		//simply prints the scope of the rows
		Proof proof = new Proof();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		//proof.printProofRowScope();		
	}
	
	public static void isInScopeIntervallTest(){
		//simply prints the scope of the intervalls
		Proof proof = new Proof();
		proof.addRow(); //0
		proof.openBox();
		proof.addRow(); //1
		proof.closeBox();
		proof.addRow(); //2
		proof.openBox();
		proof.addRow(); //3
		proof.deleteRow(2);
		proof.addRow();
		proof.openBox();
		proof.openBox();
		proof.addRow();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.openBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		proof.closeBox();
		proof.addRow();
		//proof.printProof();
		//proof.printProofIntervallScope();
		//ProofData data = proof.getData();
		//System.out.println( "boxes row 1: "+proof.getData().getRow(1).getBoxes() );
		//System.out.println( "boxes row 2: "+proof.getData().getRow(2).getBoxes() );
	}
	
	public static void boxTest(){
		Proof proof = new Proof();
		proof.addRow();
		proof.updateFormulaRow("1", 1);
		proof.openBox();
		proof.addRow();
		proof.updateFormulaRow("2", 2);
		proof.openBox();
		proof.addRow();
		proof.updateFormulaRow("3", 3);
		proof.closeBox();
		proof.addRow();
		proof.updateFormulaRow("4", 4);
		proof.closeBox();
		proof.addRow();
		proof.updateFormulaRow("5", 5);
		proof.closeBox();
		proof.addRow();
		proof.updateFormulaRow("6", 6);
		proof.printProof();
		System.out.println("================");
		proof.deleteRow(2);
		proof.printProof();
		System.out.println("================");
		proof.deleteRow(3);
		proof.deleteRow(2);
		
		
		
		proof.printProof();
	}
	
}
