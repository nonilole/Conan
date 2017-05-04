package model;

import model.rules.*;
import start.Constants;

public class RuleMapper {
    public static Rule getRule(String rule) {
        switch (rule) {
            case Constants.conjunctionElim1:
                return new ConjunctionElim(1);
            case Constants.conjunctionElim2:
                return new ConjunctionElim(2);
            case Constants.conjunctionIntro:
            	return new ConjunctionIntro();
            case Constants.disjunctionIntro1:
                return new DisjunctionIntro(1);
            case Constants.disjunctionIntro2:
                return new DisjunctionIntro(2);
            case Constants.disjunctionElim:
                return new DisjunctionElim();
            case Constants.doubleNegationElim:
                return new DoubleNegationElim();
            case Constants.equalityIntro:
                return new EqualityIntro();
            case Constants.equalityElim:
                return new EqualityElim();
            case Constants.implicationIntro:
                return new ImplicationIntro();
            case Constants.implicationElim:
                return new ImplicationElim();
            case Constants.contradictionElim:
                return new ContradictionElim();
            case Constants.existsElim:
                return new ExistsElim();
            case Constants.existsIntro:
                return new ExistsIntro();
            case Constants.forallElim:
                return new ForallElim();
            case Constants.forallIntro:
                return new ForallIntro();
            case Constants.negationElim:
                return new NegationElim();
            case Constants.negationIntro:
                return new NegationIntro();
            case Constants.premise:
                return new Premise();
            case Constants.assumption:
                return new Assumption();
            case Constants.freshVar:
                return new FreshVar();
            case Constants.modusTollens:
                return new ModusTollens();
            case Constants.proofByContradiction:
                return new ProofByContradiction();
            case Constants.lawOfExcludedMiddle:
                return new LawOfExcludedMiddle();
            case Constants.doubleNegationIntro:
                return new DoubleNegationIntro();
            case Constants.copy:
                return new Copy();
            default:
                if (rule.matches("^(∃|∀)[a-z](i|e)$")) {
                    if (rule.charAt(0) == '∃') {
                        if (rule.charAt(2) == 'i') {
                            return new ExistsIntro(rule.charAt(1));
                        } else {
                            return new ExistsElim(rule.charAt(1));
                        }
                    } else {
                        if (rule.charAt(2) == 'i') {
                            return new ForallIntro(rule.charAt(1));
                        } else {
                            return new ForallElim(rule.charAt(1));
                        }
                    }
                }
                return null;
        }
    }
}
