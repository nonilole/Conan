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
    };
    @Override
    public boolean execute() {
        if (1 <= rowNo && rowNo <= rList.size()) {
            RowPane rp = rList.get(rowNo-1);
            this.expression = rp.getExpression().getText();
            this.rule = rp.getRule().getText();
//            for (int i = 0; i < 3; i++) {
//                prompt.set(i, rp.getRulePrompt(i).getText());
//            }
        }
        return proof.deleteRow(rowNo);
    }
    @Override
    public void undo() {
        proof.insertNewRow(rowNo-1, BoxReference.AFTER);
        RowPane rp = rList.get(rowNo-1);
        rp.setExpression(expression);
        rp.setRule(rule);
//        for (int i = 0; i < 3; i++) {
//            rp.setRulePrompt(i, prompt.get(i));
//        }
    }
}
