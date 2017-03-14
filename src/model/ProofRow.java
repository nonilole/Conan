package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import model.formulas.Formula;

/***
 * ProofRow is the internal representation of a row for a proof.
 */
public class ProofRow {
	private List<Box> boxes; //First box in list should be the top-level box, last box should be the innermost one
    private Formula formula;
    private String userInput;
    private String rule = "";
    private boolean isWellFormed = true;
    //private boolean ruleIsWellFormed; //user should ot type in rules, no need to track if it's wellformed
    private boolean isVerified = false;;
    //private boolean matchesConclusion; // th row doesn't need to know this

    //boxes is a list of the boxes that this row is within,
    public ProofRow(List<Box> boxes){
    	assert(boxes.size() > 0); //at least the top-level box should be in this list
    	this.boxes = boxes;
    }
    
    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public boolean isWellFormed() {
        return this.isWellFormed;
    }
    public boolean isVerified() {
        return this.isVerified;
    }

    public void setWellformed(boolean b) {
        this.isWellFormed = b;
    }
    public void setVerified(boolean b) {
        this.isVerified = b;
    }
    public Formula getFormula() {
        return this.formula;

    }
    public String getRule() {
        return this.rule;
    }
    public List<Box> getBoxes(){
    	return boxes;
    }
    
    public Box getInnermostOpenBox(){
    	Stack<Box> stack = new Stack<Box>();
    	assert(getBoxes().get(0).isOpen());//top-level box should always be open
    	for(Box box : getBoxes()){
    		stack.push(box);
    	}
    	while(stack.isEmpty() == false){
    		Box box = stack.pop();
    		if(box.isOpen()){
    			return box;
    		}
    	}
    	throw new RuntimeException("No open box in this row");
    }
    
    //returns a new list with the open boxes that contains this row
    public List<Box> getOpenBoxes(){
    	List<Box> openBoxes = new ArrayList<Box>();
    	boolean noMoreOpenBoxes = false; // for debugging/assertion
    	for(Box box : boxes){
    		if(box.isOpen()){
    			assert( noMoreOpenBoxes == false);
    			openBoxes.add(box);
    		}
    		else{
    			//if this box is closed, all boxes inside it should be too.
    			noMoreOpenBoxes = true;
    		}
    	}
    	return openBoxes;
    }
    
    @Override //TODO: also represent the rule...
    public String toString(){
    	if(formula == null){
    		return "";
    	}
    	else{
    		return formula.toString();
    	}
    }

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
}
