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
        ruleToClass.put("Premise", Premise.class);
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
}
