package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

public class AddRow implements Command {
    Proof proof;
    int rowIdx;
    String expression = "";
    String rule = "";
    List<String> prompt = new ArrayList<String>(3);
    List<RowPane> rList;
    public AddRow(Proof proof, List<RowPane> rList) {
        this.proof = proof;
        this.rList = rList;
        prompt.add("");
        prompt.add("");
        prompt.add("");
    };
    @Override
    public boolean execute() {
        proof.addRow();
        this.rowIdx = rList.size()-1;
        RowPane rp = rList.get(rowIdx);
        rp.setExpression(expression);
        rp.setRule(rule);
        for (int i = 0; i < 3; i++) {
            rp.setRulePrompt(i, prompt.get(i));
        }
        rp.getExpression().requestFocus();
        return true;
    }

    @Override
    public void undo() {
        RowPane rp = rList.get(rowIdx);
        this.expression = rp.getExpression().getText();
        this.rule = rp.getRule().getText();
        for (int i = 0; i < 3; i++) {
            this.prompt.set(i, rp.getRulePrompt(i).getText());
        }
        proof.deleteRow(rowIdx+1);
    }

    @Override
    public String toString() {
        return "Added row to " + (rowIdx+1);
    }
}
