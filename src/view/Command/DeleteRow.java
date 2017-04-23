package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

public class DeleteRow implements Command {
    Proof proof;
    List<RowPane> rList;
    int rowNo;
    String expression;
    String rule;
    List<String> prompt = new ArrayList<String>(3);
    public DeleteRow(Proof proof, int rowNo, List<RowPane> rList) {
        this.proof = proof;
        this.rowNo = rowNo;
        this.rList = rList;
        prompt.add("");
        prompt.add("");
        prompt.add("");
    };
    @Override
    public boolean execute() {
        if (1 <= rowNo && rowNo <= rList.size()) {
            RowPane rp = rList.get(rowNo-1);
            this.expression = rp.getExpression().getText();
            this.rule = rp.getRule().getText();
            for (int i = 0; i < 3; i++) {
                this.prompt.set(i, rp.getRulePrompt(i).getText());
            }
        }
        return proof.deleteRow(rowNo);
    }
    @Override
    public void undo() {
        if (rowNo == -1) {
            proof.addRow();
        } else if (rowNo == 1) {
            proof.insertNewRow(rowNo, BoxReference.BEFORE);
        } else {
            proof.insertNewRow(rowNo-1, BoxReference.AFTER);
        }
        RowPane rp = rList.get(rowNo-1);
        rp.setExpression(expression);
        rp.setRule(rule);
        for (int i = 0; i < 3; i++) {
            rp.setRulePrompt(i, prompt.get(i));
        }
        rp.getExpression().requestFocus();
    }
    @Override
    public String toString() {
        return "Delete row to " + rowNo;
    }
}
