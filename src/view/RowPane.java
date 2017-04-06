package view;

import javafx.scene.CacheHint;
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
	
	public TextField getExpression() {
		return (TextField) this.getCenter(); 
	}
	public TextField getRule() {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(0);
	}
	
	public TextField getRulePrompt1() {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(1);
	}
	
	public TextField getRulePrompt2() {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(2);
	}
	
	public TextField getRulePrompt3() {
		RulePane rulePane = (RulePane) this.getRight();
		return (TextField) rulePane.getChildren().get(3);
	}

	public void hidePrompts() {
	    getRulePrompt1().setVisible(false);
		getRulePrompt2().setVisible(false);
		getRulePrompt3().setVisible(false);
	}

	public void setPrompts(int n) {
	    hidePrompts();
	    switch (n) {
            case 3:
                getRulePrompt3().setVisible(true);
            case 2:
                getRulePrompt2().setVisible(true);
            case 1:
                getRulePrompt1().setVisible(true);
                break;
            default:
                break;
        }
    }

}
