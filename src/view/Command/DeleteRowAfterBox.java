package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

public class DeleteRowAfterBox implements Command {
    Proof proof;
    int rowNo;
    String expression = "";
    String rule = "";
    List<String> prompt = new ArrayList<String>(3);
    BoxReference br;
    List<RowPane> rList;
    public DeleteRowAfterBox(Proof proof, int rowNo, List<RowPane> rList) {
        this.proof = proof;
        this.rowNo = rowNo;
        this.br = br;
        this.rList = rList;
        prompt.add("");
        prompt.add("");
        prompt.add("");
    };
    @Override
    public boolean execute() {
        RowPane rp = rList.get(rowNo);
        this.expression = rp.getExpression().getText();
        this.rule = rp.getRule().getText();
        for (int i = 0; i < 3; i++) {
            prompt.set(i, rp.getRulePrompt(i).getText());
        }
        proof.deleteRowAfterBox(rowNo);
        rList.get(rowNo-1).getExpression().requestFocus();
        return true;
    }

    @Override
    public void undo() {
        proof.addRowAfterBox(rowNo);
        RowPane rp = rList.get(rowNo);
        rp.setExpression(expression);
        rp.setRule(rule);
        for (int i = 0; i < 3; i++) {
            rp.setRulePrompt(i, prompt.get(i));
        }
    }
}
