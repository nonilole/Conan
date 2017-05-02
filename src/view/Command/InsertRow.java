package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

public class InsertRow implements Command {
    Proof proof;
    int rowNo;
    String expression = "";
    String rule = "";
    List<String> prompt = new ArrayList<String>(3);
    BoxReference br;
    List<RowPane> rList;
    public InsertRow(Proof proof, int rowNo, BoxReference br, List<RowPane> rList) {
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
        boolean result = proof.insertNewRow(rowNo, br, 0);
        if (result) {
            int offset = br == BoxReference.BEFORE ? 0 : 1;
            RowPane rp = rList.get(rowNo-1+offset);
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
        int offset = br == BoxReference.BEFORE ? 0 : 1;
        if (1 <= rowNo+offset && rowNo+offset <= rList.size()) {
            RowPane rp = rList.get(rowNo-1+offset);
            this.expression = rp.getExpression().getText();
            this.rule = rp.getRule().getText();
            for (int i = 0; i < 3; i++) {
                prompt.set(i, rp.getRulePrompt(i).getText());
            }
        }
        proof.deleteRow(rowNo+offset);
    }
    @Override
    public String toString() {
        return "Insert row to " + rowNo;
    }
}
