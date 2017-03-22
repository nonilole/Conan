package model;

import model.formulas.Formula;

import java.io.Serializable;
import java.util.ArrayList;

public class Proof implements Serializable{
    private ArrayList<ProofListener> listeners = new ArrayList<ProofListener>();
    //private ArrayList<ProofRow> rows = new ArrayList<ProofRow>();
    private Parser parser = new Parser(); //this won't be serialized
    private Formula conclusion;
    //private int curDepth = 0;
    private Box proofData = new Box(null, true);
    //private boolean openBoxNextAddRow = false; 

    /***
     * Add a new row at the end of the proof.
     */
    public void addRow() {
    	proofData.addRow();
        for (ProofListener listener : this.listeners) {
            listener.rowAdded();
        }
        System.out.println("addRow()");
        proofData.printRows(1, 1);
        System.out.println("==========================================================");
    }
    
    /**
     * TODO: explain what the method does
     * @param rowNumber
     */
    public void deleteRow(int rowNumber){
    	if(rowNumber < 1 || rowNumber > proofData.size()){
    		throw new IllegalArgumentException();
    	}
    	proofData.deleteRow(rowNumber-1);
    	for (ProofListener listener : this.listeners) {
            listener.rowDeleted(rowNumber);
        }
    	System.out.println("deleteRow("+rowNumber+")");
    	proofData.printRows(1,1);
        System.out.println("==========================================================");
    }
    
    /**
     * TODO: explain what the method does
     * @param rowNumber
     * @param br
     */
    public void insertNewRow(int rowNumber, BoxReference br){
    	if(rowNumber < 1 || rowNumber > proofData.size()+1){
    		System.out.println("Proof.insertNewRow: incorrect rowNumber");
    		System.out.println("rows.size(): "+proofData.size()+", rowNumber: "+rowNumber);
    		return;
    	}
    	proofData.insertRow(rowNumber-1, br);
    	for(ProofListener pl : listeners){
    		pl.rowInserted(rowNumber, br);
    	}
    	System.out.println("insertNewRow("+rowNumber+", "+br+")");
    	proofData.printRows(1,1);
        System.out.println("==========================================================");
    }
    
    //Will you ever update both the formula and rule fields at the same time?
    public void updateRow(String formula, String rule, int rowNumber){}

    /**
     * Alert the listeners about the row.
     * @param formula
     * @param rowNumber
     */
    public void updateFormulaRow(String formula, int rowNumber){
    	//System.out.println("Proof.updateFormulaRow("+formula+", "+rowNumber+")");
        int rowIndex = rowNumber-1;
        boolean wellFormed = proofData.updateFormulaRow(rowIndex, formula);
        
        for (ProofListener listener : this.listeners) {
            listener.rowUpdated(wellFormed, rowNumber);
        }
        //verifyConclusion(rowIndex);
        proofData.printRows(1,1);
        System.out.println("==========================================================");
    }
    public void updateRuleRow(String rule, int rowNumber){
    	System.out.println("Proof.updateRuleRow not implemented!");
    }
    //public void saveProof(String filepath){}
    //public void loadProof(String filepath){}
    
    //should verify each line in the proof from line start
    public boolean verifyProof(int start){ 
    	System.out.println("Proof.verifyProof not implemented!");
    	return true;
    }
    
    //should verify that the row is correct with regards to it's rule
    public boolean verifyRow(int rowNumber){ 
    	System.out.println("Proof.verifyRow not implemented!");
    	return true;
    }
    
    //This shouldn't be used when a proper UI for opening boxes has been implemented
    //since at that point, you open a box in a specific row rather than at the end of a proof
    //Instead, at that point, use openBox(int rowNr)
    public void openBox() {
    	proofData.openNewBox();
        for (ProofListener listener : this.listeners) {
            listener.boxOpened();
        }
    }
    
    public void openBox(int rowNr){
    	System.out.println("Proof.openBox(int) not implemented!");
    	//if rowNr refers to the last line, the new box should be open
    	//otherwise closed
    	for (ProofListener listener : this.listeners) {
            listener.boxOpened();
        }
    }
    
    public void closeBox(){
        proofData.closeBox();
        for (ProofListener listener : this.listeners) {
            listener.boxClosed();
        }
    }
    
    /**
     * TODO: explain what the method does
     * @param conclusion
     */
    public void updateConclusion(String conclusion) {
        try {
            this.conclusion = parser.parse(conclusion);
        } catch (Exception ParseException) {
            this.conclusion = null;
            return;
        }
        for (int i = 0; i < proofData.size(); i++) {
            verifyConclusion(i);
        }
    }
    
    /**
     * TODO: explain what the method does
     * @param rowIndex
     */
    public void verifyConclusion(int rowIndex) {
    	System.out.println("Proof.verifyConclusion not implemented!");
        ProofRow row = proofData.getRow(rowIndex);
        /*if (false) {
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(false, rowIndex + 1);
            }
        }*/
        if (this.conclusion == null)
            return;
        if (row.getFormula() == null)
            return;
        if (this.conclusion.equals(row.getFormula())) {
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(true, rowIndex+1);
            }
        }
    }
    
    public void registerProofListener(ProofListener listener){
        this.listeners.add(listener);
    }
    
    public void printProof(){
    	proofData.printRows(1,1);
    	/*for(int i = 0; i < proofData.size(); i++){
    		System.out.println(i+"."+proofData.getRow(i));
    	}*/
    }
    /*
    public void printProofRowScope(){
    	proofData.printProofRowScope();
    }
    
    public void printProofIntervallScope(){
    	proofData.printProofIntervallScope();
    }*/
}
