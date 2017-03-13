package model;

import model.formulas.Formula;

import java.io.Serializable;
import java.util.ArrayList;

public class Proof implements Serializable{
    private ArrayList<ProofListener> listeners = new ArrayList<ProofListener>();
    //private ArrayList<ProofRow> rows = new ArrayList<ProofRow>();
    private Parser parser = new Parser();
    private Formula conclusion;
    //private int curDepth = 0;
    private ProofData proofData = new ProofData();

    /***
     * Add the row to the Proof representation. Add null pointer instead of Formula if not well formed.
     */
    public void addRow() {
        //rows.add(new ProofRow());
    	proofData.addRow();
        for (ProofListener listener : this.listeners) {
            listener.rowAdded();
        }
    }
    
    public void deleteRow(int rowNumber){
    	if(rowNumber < 1 || rowNumber > proofData.size()){
    		throw new IllegalArgumentException();
    	}
    	proofData.deleteRow(rowNumber-1);
    	for (ProofListener listener : this.listeners) {
            listener.rowDeleted(rowNumber);
        }
    }
    
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
    }
    public void updateRow(String formula, String rule, int rowNumber){}

    /**
     * Alert the listeners about the row.
     * @param formula
     * @param rowNumber
     */
    public void updateFormulaRow(String formula, int rowNumber){
        int rowIndex = rowNumber-1;
        ProofRow row = proofData.getRow(rowIndex);
        boolean wellFormed;
        Formula parsedFormula;
        try {
            parsedFormula = parser.parse(formula);
            wellFormed = true;
        } catch(Exception ParseException) {
            wellFormed = formula.equals("") ? true : false;
            parsedFormula = null;
        }
        row.setWellformed(wellFormed);
        row.setFormula(parsedFormula);
        for (ProofListener listener : this.listeners) {
            listener.rowUpdated(wellFormed, rowNumber);
        }
        verifyConclusion(rowIndex);
    }
    public void updateRuleRow(String rule, int rowNumber){
    	System.out.println("Proof.updateRuleRow not implemented!");
    }
    //public void saveProof(String filepath){}
    //public void loadProof(String filepath){}
    public boolean verifyProof(int start){ 
    	System.out.println("Proof.verifyProof not implemented!");
    	return true;
    }
    public boolean verifyRow(int rowNumber){ 
    	System.out.println("Proof.verifyRow not implemented!");
    	return true;
    }
    public void openBox() {
        //TODO: something...
        for (ProofListener listener : this.listeners) {
            listener.boxOpened();
        }
    }
    public void closeBox(){
        //TODO: something
        for (ProofListener listener : this.listeners) {
            listener.boxClosed();
        }
    }
    
    public void updateConclusion(String conclusion) {
        try {
            this.conclusion = parser.parse(conclusion);
        } catch (Exception ParseException) {
            this.conclusion = null;
        }
        for (int i = 0; i < proofData.size(); i++) {
            verifyConclusion(i);
        }
    }
    
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
}
