package view.Command;

import model.Proof;
import view.RowPane;

import java.util.List;

public class DeleteBox implements Command {
    Proof proof;
    int rowNo;
    public DeleteBox(Proof proof, int rowNo, List<RowPane> rList) {
        this.proof = proof;
        this.rowNo = rowNo;
    };

    @Override
    public boolean execute() {
        return proof.removeBox(rowNo-1);
    }

    @Override
    public void undo() {
        proof.insertBox(rowNo-1);
    }
    @Override
    public String toString() {
        return "Insert box to " + rowNo;
    }
}
