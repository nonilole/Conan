package view;

import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

// Only GUI no logic"
public class RulePane extends FlowPane {
    public RulePane() {
        this.setMaxWidth(340);
        TextField tfRule = new TextField();
        tfRule.setPromptText("Rule");
        TextField tfRulePrompt1 = new TextField();
        TextField tfRulePrompt2 = new TextField();
        TextField tfRulePrompt3 = new TextField();
        //set the rule prompts to invisible
        tfRulePrompt1.setVisible(false);
        tfRulePrompt2.setVisible(false);
        tfRulePrompt3.setVisible(false);
        tfRule.setId("rightTextField");
        tfRulePrompt1.setId("rulePromt1tf");
        tfRulePrompt2.setId("rulePromt2tf");
        tfRulePrompt3.setId("rulePromt3tf");
        tfRule.getStyleClass().add("myText");
        tfRulePrompt1.getStyleClass().add("myText");
        tfRulePrompt2.getStyleClass().add("myText");
        tfRulePrompt3.getStyleClass().add("myText");
        tfRule.setMaxWidth(100);
        tfRulePrompt1.setMaxWidth(80);
        tfRulePrompt2.setMaxWidth(80);
        tfRulePrompt3.setMaxWidth(80);
        //adding the textfield for the rule and the rulepromts
        this.getChildren().addAll(tfRule, tfRulePrompt1, tfRulePrompt2, tfRulePrompt3);
    }
}
