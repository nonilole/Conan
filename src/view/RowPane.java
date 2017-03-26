package view;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class RowPane extends BorderPane{
	
	private boolean isFirstRowInBox;
	//nrOfClosingBoxes tracks how many boxes that this row is the last line of
	private int nrOfClosingBoxes;
	
	public RowPane(boolean isFirstRowInBox, int nrOfClosingBoxes){
		super();
		this.isFirstRowInBox = isFirstRowInBox;
		this.setNrOfClosingBoxes(nrOfClosingBoxes);
	}
	
	public void setIsFirstRowInBox(boolean b){
		isFirstRowInBox = b;
	}
	
	public boolean getIsFirstRowInBox(){
		return isFirstRowInBox;
	}

	public int getNrOfClosingBoxes() {
		return nrOfClosingBoxes;
	}

	public void setNrOfClosingBoxes(int nrOfClosingBoxes) {
		this.nrOfClosingBoxes = nrOfClosingBoxes;
	}
	
	public void incrementNrOfClosingBoxes(){
		nrOfClosingBoxes++;
	}
	
	public TextField getExpression() {
		TextField expression = new TextField(); 
		expression = (TextField) this.getCenter(); 
		return expression;
	}
	public TextField getRule() {
		RulePane rulePane = new RulePane();
		rulePane = (RulePane) this.getRight();
		TextField rule = new TextField();
		rule = (TextField) rulePane.getChildren().get(0);
		return rule; 
	}
	
	public TextField getRulePrompt1() {
		RulePane rulePane = new RulePane();
		rulePane = (RulePane) this.getRight();
		TextField ruleprompt1 = new TextField();
		ruleprompt1 = (TextField) rulePane.getChildren().get(1);
		return ruleprompt1;
	}
	
	public TextField getRulePrompt2() {
		RulePane rulePane = new RulePane();
		rulePane = (RulePane) this.getRight();
		TextField ruleprompt2 = new TextField();
		ruleprompt2 = (TextField) rulePane.getChildren().get(1);
		return ruleprompt2;
	}
	
	public TextField getRulePrompt3() {
		RulePane rulePane = new RulePane();
		rulePane = (RulePane) this.getRight();
		TextField ruleprompt3 = new TextField();
		ruleprompt3 = (TextField) rulePane.getChildren().get(1);
		return ruleprompt3;
	}
}
