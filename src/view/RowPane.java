package view;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

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
	public void decrementNrOfClosingBoxes(){ nrOfClosingBoxes--; }

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
	
	public void hideAndClearPrompts() {
	    for (int i = 0; i < 3; i++) {
			getRulePrompt(i).setVisible(false);
			getRulePrompt(i).clear();
		}
	}

	public void setPrompts(int n) {
		hideAndClearPrompts();
	    this.numberOfPrompts = n;
	    TextField tf;
	    switch (n) {
            case 3:
                getRulePrompt(2).setVisible(true);
                tf = getRulePrompt(2);
                tf.setPromptText("Rows");
            case 2:
                getRulePrompt(1).setVisible(true);
                tf = getRulePrompt(1);
                tf.setPromptText("Rows");
            case 1:
                getRulePrompt(0).setVisible(true);
                tf = getRulePrompt(0);
                tf.setPromptText("Rows");
                break;
            default:
                break;
        }
    }

}
