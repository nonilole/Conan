package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 4/23/17.
 */
public class AddRowAfterBox implements Command{
    Proof proof;
    int rowNo;
    String expression = "";
    String rule = "";
    List<String> prompt = new ArrayList<String>(3);
    BoxReference br;
    List<RowPane> rList;
    public AddRowAfterBox(Proof proof, int rowNo, List<RowPane> rList) {
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
        boolean result = proof.insertNewRow(rowNo, BoxReference.AFTER, 1);
        if (result) {
            RowPane rp = rList.get(rowNo);
            rp.setExpression(expression);
            rp.setRule(rule);
            for (int i = 0; i < 3; i++) {
                rp.setRulePrompt(i, prompt.get(i));
            }
            rp.getExpression().requestFocus();
        }
        return result;
    }

    @Override
    public void undo() {
        RowPane rp = rList.get(rowNo);
        this.expression = rp.getExpression().getText();
        this.rule = rp.getRule().getText();
        for (int i = 0; i < 3; i++) {
            prompt.set(i, rp.getRulePrompt(i).getText());
        }
        proof.deleteRowAfterBox(rowNo);
    }
    @Override
    public String toString() {
        return "Adding row after box that has row " + rowNo;
    }
}
