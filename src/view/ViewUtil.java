package view;

import javafx.scene.control.TextField;

public class ViewUtil {
    public static void addFocusListener(TextField tf, ProofView pv) {
        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            pv.lastFocusedTf = tf;
            pv.caretPosition = tf.getCaretPosition();
        });
    }
    public static String checkShortcut(String newValue) {
        newValue = newValue.replaceAll("!|ne|no", "¬");
        newValue = newValue.replaceAll("&|an", "∧");
        newValue = newValue.replaceAll("->|im", "→");
        newValue = newValue.replaceAll("fa|fo", "∀");
        newValue = newValue.replaceAll("or", "∨");
        newValue = newValue.replaceAll("ex|te|th", "∃");
        newValue = newValue.replaceAll("co|bo", "⊥");
        return newValue;
    }
}
