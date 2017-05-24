package model.rules;

import java.io.Serializable;

import model.Box;
import model.VerificationInputException;
import model.formulas.Formula;


public abstract class Rule implements Serializable{

    //if this is false there is no reason to continue the verification
    //since info is missing
    public abstract boolean hasCompleteInfo();

    //Update the reference in the rule object
    public abstract void updateReference(int index, String newValue);

    public abstract boolean verifyReferences(Box data, int rowIndex);

    public abstract boolean verifyRow(Box data, int rowIndex);

    public abstract Formula generateRow(Box data);

    public Formula generateFormula(Box data, int rowIndex) {
        if (verifyReferences(data, rowIndex))
            return generateRow(data);
        return null;
    }

    public boolean verify(Box data, int rowIndex) {
        if (verifyReferences(data, rowIndex) == false)
            return false;
        if (data.getRow(rowIndex).getFormula() == null)
            throw new VerificationInputException("Invalid formula syntax.");
        return verifyRow(data, rowIndex);
    }
    
    public abstract String[] getReferenceStrings();
	public abstract String getDisplayName();
}
/*
public interface Rule extends Serializable{
	
	//if this is false there is no reason to continue the verification
	//since info is missing
	public boolean hasCompleteInfo();
	//Update the reference in the rule object
	public void updateReference(int index, String newValue);
	public boolean verify(Box data, int rowIndex);
	public Formula generateFormula(Box data, int rowIndex);
	public String[] getReferenceStrings();
	public String getDisplayName();
}*/
