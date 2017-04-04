package model.rules;

import model.Box;
import model.ProofRow;

public class Assumption implements Rule {
    @Override
    public String toString() {
        return "Assumption";
    }

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public void updateReference(int index, String newValue) {
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        ProofRow rowToVerify = data.getRow(rowIndex);
        // Check if first row of box and box is an actual box.
        Box parent = rowToVerify.getParent();
        if (parent.isTopLevelBox())
            return false;
        return parent.getRow(0).equals(rowToVerify);
    }
}
