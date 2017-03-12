package model;

import model.formulas.Formula;

import java.io.Serializable;
import java.util.ArrayList;

public class Proof implements Serializable{
    private ArrayList<ProofListener> listeners;
    private ArrayList<ProofRow> rows = new ArrayList<ProofRow>();
    private Parser parser;
    private Formula conclusion;
    private int curDepth;

    public Proof() {
        listeners = new ArrayList<ProofListener>();
        parser = new Parser();
        curDepth = 0;
    }

    /***
     * Add the row to the Proof representation. Add null pointer instead of Formula if not well formed.
     */
    public void addRow() {
        rows.add(new ProofRow());
        for (ProofListener listener : this.listeners) {
            listener.rowAdded();
        }
    }
    
    public void deleteRow(int rowNumber){
    	if(rowNumber < 1 || rowNumber > rows.size()){
    		throw new IllegalArgumentException();
    	}
    	rows.remove(rowNumber-1);
    	for (ProofListener listener : this.listeners) {
            listener.rowDeleted(rowNumber);
        }
    }
    
    public void deleteRow() {
    	System.out.println("Wrong: Proof.deleRow()");
        /*if (rows.size() >= 0) {
            rows.remove(rows.size() - 1);
            for (ProofListener listener : this.listeners) {
                listener.rowDeleted();
            }
        }*/
    }
    
    public void insertNewRow(int rowNumber, BoxReference br){
    	if(rowNumber < 1 || rowNumber > rows.size()+1){
    		System.out.println("Proof.insertNewRow: incorrect rowNumber");
    		System.out.println("rows.size(): "+rows.size()+", rowNumber: "+rowNumber);
    		return;
    	}
    	//System.out.println("Proof("+rowNumber+","+br+")");
    	rows.add(rowNumber-1, new ProofRow());
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
        ProofRow row = rows.get(rowIndex);
        boolean wellFormed;
        Formula parsedFormula;
        try {
            parsedFormula = parser.parse(formula);
            wellFormed = true;
        } catch(Exception ParseException) {
            wellFormed = formula.equals("") ? true : false;
            parsedFormula = null;
        }
        row.setFormulaWellformed(wellFormed);
        row.setFormula(parsedFormula);
        for (ProofListener listener : this.listeners) {
            listener.rowUpdated(wellFormed, rowNumber);
        }
        verifyConclusion(rowIndex);
    }
    public void updateRuleRow(String rule, int rowNumber){}
    public void saveProof(String filepath){}
    public void loadProof(String filepath){}
    public boolean verifyProof(int start){return true;}
    public boolean verifyRow(int rowNumber){return true;}
    public void openBox() {
        curDepth++;
        for (ProofListener listener : this.listeners) {
            listener.boxOpened();
        }
    }
    public void closeBox(){
        if (curDepth < 0)
            return;
        --curDepth;
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

        //ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < rows.size(); i++) {
            // non-parallel
            verifyConclusion(i);
            /*
            final int I = i;
            executor.submit(() -> {
                System.out.println(I);
                verifyConclusion(I);
            });
            */
        }
    }
    public void verifyConclusion(int rowIndex) {
        ProofRow row = rows.get(rowIndex);
        if (row.matchesConclusion()) {
            row.setMatchesConclusion(false);
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(false, rowIndex + 1);
            }
        }
        if (this.conclusion == null)
            return;
        if (row.getFormula() == null)
            return;
        if (this.conclusion.equals(row.getFormula())) {
            row.setMatchesConclusion(true);
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(true, rowIndex+1);
            }
        }
    }
    public void registerProofListener(ProofListener listener){
        this.listeners.add(listener);
    }
    public Rule createCustomRule(){return null;}
}
