package view;

import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class RulePane extends FlowPane{

	public RulePane() {
		this.setMaxWidth(340);
		TextField tfRule = new TextField();
		TextField tfRulePromt1 = new TextField();
		TextField tfRulePromt2 = new TextField();
		TextField tfRulePromt3 = new TextField();

		//set the rule prompts to invisible
		tfRulePromt1.setVisible(false);
		tfRulePromt2.setVisible(false);
		tfRulePromt3.setVisible(false);

		tfRule.setId("rightTextfield");
		tfRulePromt1.setId("rulePromt1tf");
		tfRulePromt2.setId("rulePromt2tf");
		tfRulePromt3.setId("rulePromt3tf");

		tfRule.getStyleClass().add("myText");
		tfRulePromt1.getStyleClass().add("myText");
		tfRulePromt2.getStyleClass().add("myText");
		tfRulePromt3.getStyleClass().add("myText");

		tfRule.setMaxWidth(100);
		tfRulePromt1.setMaxWidth(80);
		tfRulePromt2.setMaxWidth(80);
		tfRulePromt3.setMaxWidth(80);

		//adding the textfield for the rule and the rulepromts
		this.getChildren().addAll(tfRule, tfRulePromt1, tfRulePromt2, tfRulePromt3);
	}
}
