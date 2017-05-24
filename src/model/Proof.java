package model;

import model.formulas.Formula;
import model.rules.Premise;
import model.rules.Rule;
import start.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import static java.util.prefs.Preferences.userRoot;

public class Proof implements Serializable{
    private transient ArrayList<ProofListener> listeners = new ArrayList<ProofListener>();
    private transient Parser parser = new Parser(); //this won't be serialized
    private Formula conclusion;
    private Box proofData = new Box(null, true);
    public boolean isLoaded = false;
    
    
    public Proof(){}
    
    public String getProofString() {
        return proofData.rowsToString(1);
    }

    /***
     * Add a new row at the end of the proof.
     */
    public void addRow() {
        proofData.addRow();
        for (ProofListener listener : this.listeners) {
            listener.rowAdded();
        }
        verifyProof();
        //verifyRow(proofData.size() - 1);
        printProof("Row added");
    }

    /**
     * delete the row at index rowNumber-1
     * TODO: update indexes of rule objects in rows below?
     * TODO: verify rows below since they now might not be correct anymore
     *
     * @param rowNumber
     */

    public int deleteRow(int rowNumber) {
        if (rowNumber < 1 || rowNumber > proofData.size() || proofData.size() == 1) {
            return -1;
        }
        boolean updatedPremises = false;
        if(proofData.getRow(rowNumber-1).getRule() instanceof Premise){
        	updatedPremises = true;
        }
        System.out.println(proofData.size());
        int delDepth = (proofData.deleteRow(rowNumber - 1));
        if (delDepth == -1)
            return -1;
        for (ProofListener listener : this.listeners) {
            listener.rowDeleted(rowNumber);
        }
        if(updatedPremises){
        	this.updatedPremises();
        }
        verifyProof();
        printProof("Row deleted");
        return delDepth;
    }

    /**
     * Inserts a new row into the same box as the referenced row
     * TODO: update indexes of rule objects in rows below?
     * TODO: verify rows below since they now might not be correct anymore
     *
     * @param rowNumber: the number of the row used as reference
     * @param br:        Indicates whether the new row should be added before or after the reference row
     */

    public boolean insertNewRow(int rowNumber, BoxReference br, int depth) {
        if (rowNumber > proofData.size() + 1) {
            System.out.println("Proof.insertNewRow: incorrect rowNumber");
            System.out.println("rows.size(): " + proofData.size() + ", rowNumber: " + rowNumber);
            return false;
        }
        proofData.insertRow(rowNumber - 1, br, depth);
        for (ProofListener pl : listeners) {
            pl.rowInserted(rowNumber, br, depth);
        }
//        verifyProof(rowNumber-1 + (br == BoxReference.BEFORE ? 0 : 1));
        verifyProof();
        printProof("Inserted new row");
        return true;
    }

    public boolean deleteRowAfterBox(int rowNumber) {
        if (rowNumber < 1 || rowNumber > proofData.size() + 1) {
            return false;
        }
        proofData.deleteRowAfterBox(rowNumber - 1);
        for (ProofListener pl : listeners) {
            pl.deletedRowAfterBox(rowNumber);
        }
        verifyProof();
        printProof("Deleted row");
        return true;
    }

    //Will you ever update both the formula and rule fields at the same time?
    public void updateRow(String formula, String rule, int rowNumber) {
    }

    /**
     * Alert the listeners about the row.
     *
     * @param strFormula
     * @param rowNumber
     */
    public void updateFormulaRow(String strFormula, int rowNumber) {
        //System.out.println("Proof.updateFormulaRow("+formula+", "+rowNumber+")");
        int rowIndex = rowNumber - 1;
        ProofRow toBeUpdated = proofData.getRow(rowIndex);
        Formula parsedFormula = null;
        boolean wellFormed;
        try {
            parsedFormula = parser.parse(strFormula);
            wellFormed = true;
        } catch (ParseException e) {
            wellFormed = false;
        }
        String parsedString = parsedFormula == null ? "" : parsedFormula.parenthesize();
        toBeUpdated.setFormula(parsedFormula);
        toBeUpdated.setUserInput((strFormula == null ? "" : strFormula));
        toBeUpdated.setWellformed(wellFormed);

        for (ProofListener listener : this.listeners) {
            listener.updateParsingStatus(rowNumber, parsedString);
            listener.rowUpdated(null, wellFormed, rowNumber);
        }
//        verifyProof(rowIndex); //should use verifyProof later probably, to verify rows lower in the proof aswell
        verifyProof();
        if(toBeUpdated.getRule() instanceof Premise){
        	this.updatedPremises();
        }
        printProof("Updated formula in row");
    }

    public void updateRuleRow(String ruleString, int rowNumber) throws IllegalAccessException, InstantiationException {
        //System.out.println("updateRuleRow: rule=" + ruleString + ", rowNr=" + rowNumber);
        int rowIndex = rowNumber - 1;
        boolean premiseUpdate = false;
        ProofRow pr = proofData.getRow(rowIndex);
        if(pr.getRule() instanceof Premise){
            premiseUpdate = true;
        }
        Rule rule = RuleMapper.getRule(ruleString);
        if(ruleString.equals(Constants.premise)){
        	premiseUpdate = true;
        }
        pr.setRule(rule);
//        verifyProof(rowIndex);
        verifyProof();
        if(premiseUpdate){
        	this.updatedPremises();
        }
        printProof("Updated rule in row");
    }

    //Adds a rule object to the given row
    public void addRule(int rowNr, Rule rule) {
        System.out.println("addRule: " + rowNr + ", Rule: " + rule);
        proofData.getRow(rowNr - 1).setRule(rule);
       // verifyRow(rowNr - 1);
        verifyProof();
    }

    //should verify each line in the proof from line startIndex
    public boolean verifyProof() {
        return verifyProof(0);
    }
    public boolean verifyProof(int startIndex) {
        assert (startIndex < proofData.size()) : "Proof.verifyProof: index out of bounds";
        boolean returnValue = true;
        for (int i = startIndex; i < proofData.size(); i++) {
            try {
                if (verifyRow(i) == false) returnValue = false;
                for (ProofListener listener : this.listeners) {
                    listener.updateErrorStatus(i + 1, "");
                }
            } catch (VerificationInputException e) {
                for (ProofListener listener : this.listeners) {
                    listener.updateErrorStatus(i+1, e.getMessage());
                }
            }
            //TODO: inform listeners about each row
        }
        for (ProofListener listener : this.listeners) {
            listener.updateStatus();
        }

        return returnValue;
    }

    //should verify that the row is correct with regards to it's rule and
    //update the row object with this info
    public boolean verifyRow(int rowIndex) {
        assert (rowIndex < proofData.size()) : "Proof.verifyRow: index out of bounds";
        ProofRow row = proofData.getRow(rowIndex);
        Rule rule = row.getRule();
        boolean isVerified = false;
        VerificationInputException exceptionToThrow = null;
        if (rule == null) {
            exceptionToThrow = new VerificationInputException("Invalid rule syntax.");
        } else if (rule.hasCompleteInfo() == false) {
            exceptionToThrow = new VerificationInputException("A reference is empty.");
        } else {
            try {
                isVerified = rule.verify(proofData, rowIndex);
            } catch (VerificationInputException e) {
                exceptionToThrow = e;
            }
        }
        row.setVerified(isVerified);
        for (ProofListener listener : this.listeners) {
            listener.rowVerified(isVerified, rowIndex + 1);
        }
        verifyConclusion(rowIndex);
        if (exceptionToThrow != null)
            throw exceptionToThrow;
        return isVerified;
    }

    public boolean insertBox(int rowIndex) {
        ProofRow pr = proofData.getRow(rowIndex);
        if (pr.getParent() != null && !pr.getParent().isTopLevelBox()) {
            if (pr.getParent().getRow(0) == pr) {
                return false; // Don't insert box if it's the first row in a box.
            }
        }
        proofData.insertBox(rowIndex);
        for (ProofListener listener : this.listeners) {
            listener.boxInserted(rowIndex + 1);
        }
//        verifyProof(rowIndex);
        verifyProof();
        printProof("inserted box");
        return true;
    }

    public boolean removeBox(int rowIndex) {
        // We assume this is possible by precondition
        proofData.removeBox(rowIndex);
        for (ProofListener listener : this.listeners) {
            listener.boxRemoved(rowIndex + 1);
        }
        verifyProof();
        return true;
    }

    /**
     * Updates the conclusion used as the 'goal' of the proof
     *
     * @param conclusion
     */
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

    /**
     * Verifies if the row matches the conclusion
     *
     * @param rowIndex: index of the row to verify
     */
    //TODO: check that the row has been verified, not just matches conclusion
    public void verifyConclusion(int rowIndex) {
        ProofRow row = proofData.getRow(rowIndex);
        /*if (false) {
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(false, rowIndex + 1);
            }
        }*/
        if (this.conclusion != null && row.getFormula() != null && row.isVerified()
                && this.conclusion.equals(row.getFormula()) && row.getParent().isTopLevelBox()) {
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(true, rowIndex + 1);
            }
        } else {
            for (ProofListener listener : this.listeners) {
                listener.conclusionReached(false, rowIndex + 1);
            }
        }
    }

    /**
     * Needs to be called after a proof has been loaded/deserialized
     */
    public void load() {
        parser = new Parser();
        this.listeners = new ArrayList<ProofListener>();
        isLoaded = true;
    }

    public void registerProofListener(ProofListener listener) {
        this.listeners.add(listener);
    }

    //Only for debugging, do not use this for actual implementation
    public Box getData() {
        return proofData;
    }

    public void printBoxes() {
        proofData.printBoxes();
    }

    public void printProof(boolean zeroBasedNumbering) {
        int x = zeroBasedNumbering ? 0 : 1;
        proofData.printRows(1, x);
    }

    public void rulePromptUpdate(int rowNr, int promptNumber, String newValue) {
        //System.out.println("rulePromptUpdate rowNr:" + rowNr + " promptNumber:" + promptNumber + " newValue:" + newValue);
        int rowIndex = rowNr - 1;
        ProofRow row = proofData.getRow(rowNr - 1);

        Rule rule = row.getRule();
        if (rule == null)
            return;
        //System.out.println(rowIndex);
        //System.out.println(rule.toString());
        try {
            rule.updateReference(promptNumber, newValue);
        }
        //if the string is not of the correct format ie an integer or interval
        catch (NumberFormatException e) {
            System.out.println("Incorrect reference format");
        }
        //if the promptNumber does not match the rule object, for example ConjunctionIntro has 2 references,
        //so promptNumber = 3 wouldn't make sense
        catch (IllegalArgumentException e) {
            System.out.println("Invalid argument for " + rule.getClass().getSimpleName());
        }
        generateRow(rowIndex);
//        verifyProof(rowIndex);
        verifyProof();
        printProof("rulePromptUpdate");
    }

    private void generateRow(int rowIndex) {
        Preferences prefs = userRoot().node("General");
        if (!prefs.getBoolean("generate", true))
            return;
        assert (rowIndex < proofData.size());
        ProofRow row = proofData.getRow(rowIndex);
        Rule rule = row.getRule();
        if (rule == null || rule.hasCompleteInfo() == false || row.getFormula() != null) {
            return;
        }
        Formula generated = null;
        try {
            generated = rule.generateFormula(proofData, rowIndex);
        } catch (VerificationInputException e) {
            return;
        }
        if (generated == null)
            return;
        row.setFormula(generated);
        for (ProofListener listener : this.listeners) {
            listener.rowUpdated(generated.toString(), true, rowIndex + 1);
        }
    }
    
    public String getPremisesStr(){
    	StringBuilder strB = new StringBuilder();
    	String prefix = "";
    	for(int i = 0; i < proofData.size(); i++){
    		ProofRow row = proofData.getRow(i);
    		if(row.getRule() == null) 
    			continue;
    		else if(row.getRule() instanceof Premise){
    			strB.append( prefix +(row.getFormula() == null ? row.getUserInput() : row.getFormula()+"") );
    			prefix = ", ";
    		}
    	}
    	return strB.toString();
    }
    
    public String getConclusionStr(){
    	return conclusion == null ? "" : conclusion+"";
    }

    public void printRowScopes(boolean zeroBasedNumbering) {
        proofData.printRowScopes(zeroBasedNumbering);
    }


    public void printIntervalScopes(boolean zeroBasedNumbering) {
        proofData.printIntervalScopes(zeroBasedNumbering);

    }
    
    public List<ProofListener.RowInfo> getProofInfo(){
    	ArrayList<ProofListener.RowInfo> returnList = new ArrayList<ProofListener.RowInfo>();
    	
    	proofData.fillList(returnList);
    	return returnList;
    }
    
    public void printProof(String updateAction){
    	System.out.println(updateAction);
    	System.out.println("Premises: "+getPremisesStr()+", Conclusion: "+conclusion);
    	boolean zeroBasedNumbering = false;
    	int x = zeroBasedNumbering ? 0 : 1;
        proofData.printRows(1, x);
        System.out.println("===========================================================");
    }
    
    public void updatedPremises(){
    	for(ProofListener pl : this.listeners){
    		pl.premisesUpdated(this.getPremisesStr());
    	}
    }
}
