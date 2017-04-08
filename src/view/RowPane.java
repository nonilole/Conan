package view;

import javafx.scene.CacheHint;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class RowPane extends BorderPane{

	private int numberOfPrompts;
	private boolean isFirstRowInBox;
	//nrOfClosingBoxes tracks how many boxes that this row is the last line of
	private int nrOfClosingBoxes;
	
	public RowPane(boolean isFirstRowInBox, int nrOfClosingBoxes){
		super();
		this.isFirstRowInBox = isFirstRowInBox;
		this.setNrOfClosingBoxes(nrOfClosingBoxes);
		this.numberOfPrompts = 0;
		TextField tfExpression = new TextField();
		tfExpression.setId("expression");
		tfExpression.getStyleClass().add("myText");
		tfExpression.setPrefWidth(580);
		this.setCenter(tfExpression);
		this.setRight(new RulePane());
		this.setCache(true);
		this.setCacheShape(true);
		this.setCacheHint(CacheHint.SPEED);
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

	public void setExpression(String s) {
		getExpression().setText(s);
	}
	public TextField getExpression() {
		return (TextField) this.getCenter(); 
	}
	public void setRule(String s) {
		getRule().setText(s);
	}
	public TextField getRule() {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(0);
	}
	public TextField getClosestPromptFromLeft(int index) {
	    if (index > this.numberOfPrompts-1)
	    	index = this.numberOfPrompts-1;
	    if (index == -1)
	    	return getRule();
	    return getRulePrompt(index);
	}

	public void setRulePrompt(int i, String s) {
	    getRulePrompt(i).setText(s);
	}
	public TextField getRulePrompt(int index) {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(1+index);
	}
	
	public void hidePrompts() {
	    for (int i = 0; i < 3; i++) {
			getRulePrompt(i).setVisible(false);
		}
	}

	public void setPrompts(int n) {
	    hidePrompts();
	    this.numberOfPrompts = n;
	    switch (n) {
            case 3:
                getRulePrompt(2).setVisible(true);
            case 2:
                getRulePrompt(1).setVisible(true);
            case 1:
                getRulePrompt(0).setVisible(true);
                break;
            default:
                break;
        }
    }

}
