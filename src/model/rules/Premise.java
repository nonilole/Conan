package model.rules;

import model.Box;
import model.formulas.Formula;

public class Premise extends Rule {

    @Override
    public void updateReference(int a, String b) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean verifyReferences(Box data, int rowIndex) {
        return true;
    }

    @Override
    public boolean verifyRow(Box data, int rowIndex) {
        return true;
    }

    @Override
    public Formula generateRow(Box data) {
        return null;
    }

    @Override
    public boolean hasCompleteInfo() {
        return true;
    }

    @Override
    public String toString() {
        return "Premise";
    }

    @Override
    public boolean verify(Box data, int rowIndex) {
        return true;
    }

    @Override
    public Formula generateFormula(Box data, int rowIndex) {
        return null;
    }
}
