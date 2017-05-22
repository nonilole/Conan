package view;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.prefs.Preferences;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

public class ViewUtil {
    final static KeyCombination ctrlZ = new KeyCodeCombination(Z, SHORTCUT_DOWN);
    final static KeyCombination ctrlY = new KeyCodeCombination(Y, SHORTCUT_DOWN);
    static Preferences prefs = Preferences.userRoot().node("General");

    public static void addFocusListener(TextField tf, ProofView pv) {
        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            pv.lastFocusedTf = tf;
            pv.caretPosition = tf.getCaretPosition();
            pv.updateStatus();
        });
    }
    public static void applyStyleIf(TextField expression, boolean bool, String style) {
        if(prefs.getBoolean("verify",true)) {

            expression.getStyleClass().removeIf((s) -> s.equals(style));
            if (bool) {
                expression.getStyleClass().add(style);
            }
        }
    }
    public static String checkShortcut(String newValue) {
        return checkShortcut(newValue, false);
    }
    public static String checkShortcut(String newValue, Boolean extraShortcuts) {
        newValue = newValue.replaceAll("!|ne|no", "¬");
        newValue = newValue.replaceAll("&|an|\\^", "∧");
        newValue = newValue.replaceAll("->|im", "→");
        newValue = newValue.replaceAll("fa|fo", "∀");
        newValue = newValue.replaceAll("or|\\|", "∨");
        newValue = newValue.replaceAll("ex|te|th", "∃");
        newValue = newValue.replaceAll("0", "₀");
        newValue = newValue.replaceAll("1", "₁");
        newValue = newValue.replaceAll("2", "₂");
        newValue = newValue.replaceAll("3", "₃");
        newValue = newValue.replaceAll("4", "₄");
        newValue = newValue.replaceAll("5", "₅");
        newValue = newValue.replaceAll("6", "₆");
        newValue = newValue.replaceAll("7", "₇");
        newValue = newValue.replaceAll("8", "₈");
        newValue = newValue.replaceAll("9", "₉");
        newValue = newValue.replaceAll("bo", "⊥");
        if (extraShortcuts) {
            newValue = newValue.replaceAll("co", "Copy");
            newValue = newValue.replaceAll("pr", "Premise");
            newValue = newValue.replaceAll("as", "Ass.");
            newValue = newValue.replaceAll("fr", "Fresh");
        }
        return newValue;
    }
    public static void consumeKeys(TextField tf) {
        tf.setOnKeyPressed(key -> {
            if (ctrlY.match(key) || ctrlZ.match(key)) {
                key.consume();
            }
        });
    }
}
