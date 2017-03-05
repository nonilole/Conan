package model;

import java.util.ArrayList;

public class Proof {
    private ProofListener listener;
    ArrayList<ProofRow> rows = new ArrayList();
    private Parser parser;
    public Proof(ProofListener listener) {
        this.listener = listener;
        parser = new Parser();
    }

    /***
     * Add the row to the Proof representation. Add null pointer instead of Formula if not well formed.
     * @param formula
     * @param rule
     */
    public void addRow(String formula, String rule) {
        try {
            rows.add(new ProofRow(parser.parse(formula), rule));
        } catch(Exception ParseException) {
            rows.add(new ProofRow(null, rule));
        }
    }
    public void deleteRow(int rowNumber){}
    public void deleteRow(){rows.remove(rows.size()-1);}
    public void insertRow(String formula, String rule, int rowNumber){}
    public void updateRow(String formula, String rule, int rowNumber){}

    /**
     * Alert the listener about the row.
     * @param formula
     * @param rowNumber
     */
    public void updateFormulaRow(String formula, int rowNumber){
        int rowIndex = rowNumber-1;
        ProofRow row = rows.get(rowIndex);
        boolean wellFormed = true;
        try {
            row.setFormula(parser.parse(formula));
        } catch(Exception ParseException) {
            wellFormed = false;
        }
        if (!wellFormed && !formula.equals("")) {
            row.setFormula(null);
            listener.rowUpdated(false, rowNumber);
        } else {
            listener.rowUpdated(true, rowNumber);
        }
    }
    public void updateRuleRow(String rule, int rowNumber){}
    public void saveProof(String filepath){}
    public void loadProof(String filepath){}
    public boolean verifyProof(int start){return true;}
    public boolean verifyRow(int rowNumber){return true;}
    public void closeBox(){}
    public void verifyConclusion(){}
    public void registerProofListener(/*ProofListener pl*/){}
    public Rule createCustomRule(){return null;}
}
