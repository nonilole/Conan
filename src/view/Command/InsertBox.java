package view.Command;

import model.BoxReference;
import model.Proof;
import view.RowPane;

import java.util.ArrayList;
import java.util.List;

public class InsertBox implements Command {
    Proof proof;
    int rowNo;
    String expression = "";
    String rule = "";
    List<String> prompt = new ArrayList<String>(3);
    List<RowPane> rList;
    public InsertBox(Proof proof, int rowNo, List<RowPane> rList) {
        this.proof = proof;
        this.rowNo = rowNo;
        this.rList = rList;
        prompt.add("");
        prompt.add("");
        prompt.add("");
    };
    @Override
    public boolean execute() {
        boolean result = proof.openBox(rowNo-1);
        if (result) {
            RowPane rp = rList.get(rowNo);
            rp.setExpression(expression);
            rp.setRule(rule);
            for (int i = 0; i < 3; i++) {
                rp.setRulePrompt(i, prompt.get(i));
            }
        }
        return result;
    }

    @Override
    public void undo() {
        int offset = 0;
        if (1 <= rowNo && rowNo <= rList.size()) {
            RowPane rp = rList.get(rowNo-1+offset);
            this.expression = rp.getExpression().getText();
            this.rule = rp.getRule().getText();
            for (int i = 0; i < 3; i++) {
                prompt.set(i, rp.getRulePrompt(i).getText());
            }
        }
        proof.deleteRow(rowNo);
    }
    @Override
    public String toString() {
        return "Insert row to " + rowNo;
    }
}
