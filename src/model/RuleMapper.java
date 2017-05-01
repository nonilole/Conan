package model;

import model.rules.*;
import start.Constants;

import java.util.HashMap;
import java.util.Map;

public class RuleMapper {
    private static final Map<String, Class<?>> ruleToClass;

    static {
        ruleToClass = new HashMap<String, Class<?>>();
        ruleToClass.put(Constants.conjunctionElim1, ConjunctionElim1.class);
        ruleToClass.put(Constants.conjunctionElim2, ConjunctionElim2.class);
        ruleToClass.put(Constants.conjunctionIntro, ConjunctionIntro.class);
        ruleToClass.put(Constants.disjunctionIntro1, DisjunctionIntro1.class);
        ruleToClass.put(Constants.disjunctionIntro2, DisjunctionIntro2.class);
        ruleToClass.put(Constants.disjunctionElim, DisjunctionElim.class);
        ruleToClass.put(Constants.doubleNegationElim, DoubleNegationElim.class);
        ruleToClass.put(Constants.equalityIntro, EqualityIntro.class);
        ruleToClass.put(Constants.equalityElim, EqualityElim.class);
        ruleToClass.put(Constants.implicationIntro, ImplicationIntro.class);
        ruleToClass.put(Constants.implicationElim, ImplicationElim.class);
        ruleToClass.put(Constants.contradictionElim, ContradictionElim.class);
        ruleToClass.put(Constants.existsElim, ExistsElim.class);
        ruleToClass.put(Constants.existsIntro, ExistsIntro.class);
        ruleToClass.put(Constants.forallElim, ForallElim.class);
        ruleToClass.put(Constants.forallIntro, ForallIntro.class);
        ruleToClass.put(Constants.negationElim, NegationElim.class);
        ruleToClass.put(Constants.negationIntro, NegationIntro.class);
        ruleToClass.put(Constants.premise, Premise.class);
        ruleToClass.put(Constants.assumption, Assumption.class);
        ruleToClass.put(Constants.freshVar, FreshVar.class);
        ruleToClass.put(Constants.modusTollens, ModusTollens.class);
        ruleToClass.put(Constants.proofByContradiction, ProofByContradiction.class);
        ruleToClass.put(Constants.lawOfExcludedMiddle, LawOfExcludedMiddle.class);
        ruleToClass.put(Constants.doubleNegationIntro, DoubleNegationIntro.class);
        ruleToClass.put(Constants.copy, Copy.class);
    }

    public static Rule getRule(String rule) {
        Class<?> c = ruleToClass.getOrDefault(rule, null);
        if (c != null) {
            try {
                return (Rule) c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
//    public static Rule getRule(String rule) {
//        switch(rule){
//            case "∨I1":     return new DisjunctionIntro(1);
//            case "∨I2":     return new DisjunctionIntro(2);
//            case "∧I":      return new ConjunctionIntro();
//            case "∧E1":     return new ConjunctionElim(1);
//            case "∧E2":     return new ConjunctionElim(2);
//            case "→I":      return new ImplicationIntro();
//            case "¬¬E":     return new DoubleNegationElim();
//            case "=I":      return new EqualityIntro();
//            case "Premise": return new Premise();
//            default:        return null;
//        }
//    }
}
