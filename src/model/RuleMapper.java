package model;

import model.rules.*;

import java.util.HashMap;
import java.util.Map;

public class RuleMapper {
    private static final Map<String, Class<?>> ruleToClass;

    static {
        ruleToClass = new HashMap<String, Class<?>>();
        ruleToClass.put("∧E1", ConjunctionElim1.class);
        ruleToClass.put("∧E2", ConjunctionElim2.class);
        ruleToClass.put("∧I", ConjunctionIntro.class);
        ruleToClass.put("∨I1", DisjunctionIntro1.class);
        ruleToClass.put("∨I2", DisjunctionIntro2.class);
        ruleToClass.put("¬¬E", DoubleNegationElim.class);
        ruleToClass.put("=I", EqualityIntro.class);
        ruleToClass.put("→I", ImplicationIntro.class);
        ruleToClass.put("⊥E",ContradictionElim.class);
        ruleToClass.put("∃E", ExistsElim.class);
        ruleToClass.put("∃I", ExistsIntro.class);
        ruleToClass.put("∀E", ForallElim.class);
        ruleToClass.put("∀I", ForallIntro.class);
        ruleToClass.put("Premise", Premise.class);
        ruleToClass.put("Ass.", Assumption.class);
        ruleToClass.put("Fresh", FreshVar.class);
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
