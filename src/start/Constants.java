package start;
public class Constants {
    public static final String conjunction = "∧";
    public static final String disjunction = "∨";
    public static final String negation = "¬";
    public static final String equality = "=";
    public static final String implication = "→";
    public static final String contradiction = "⊥";
    public static final String elimination = "e";
    public static final String introduction = "i";
    public static final String exists = "∃";
    public static final String forall = "∀";
    public static final String one = "₁";
    public static final String two = "₂";

    public static final String conjunctionElim1 = conjunction + elimination + one;
    public static final String conjunctionElim2 = conjunction + elimination + two;
    public static final String conjunctionIntro = conjunction + introduction;
    public static final String disjunctionIntro1= disjunction + introduction + one;
    public static final String disjunctionIntro2 = disjunction + introduction + two;
    public static final String disjunctionElim = disjunction + elimination;
    public static final String doubleNegationElim = negation + negation + elimination;
    public static final String equalityIntro = equality + introduction;
    public static final String equalityElim = equality + elimination;
    public static final String implicationIntro = implication + introduction;
    public static final String implicationElim = implication + elimination;
    public static final String contradictionElim = contradiction + elimination;
    public static final String existsElim = exists + elimination;
    public static final String existsIntro = exists + introduction;
    public static final String forallElim = forall + elimination;
    public static final String forallIntro = forall + introduction;
    public static final String negationElim = negation + elimination;
    public static final String negationIntro = negation + introduction;
    public static final String premise = "Premise";
    public static final String assumption = "Ass.";
    public static final String freshVar = "Fresh";
    public static final String modusTollens = "MT";
    public static final String proofByContradiction = "PBC";
    public static final String lawOfExcludedMiddle = "LEM";
    public static final String doubleNegationIntro = negation + negation + introduction;
    public static final String copy = "Copy";

    public static final String intervalPromptText = "12-34";
    public static final String rowPromptText = "Row";
}
