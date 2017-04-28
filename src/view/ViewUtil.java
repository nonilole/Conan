package view;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

public class ViewUtil {
    final static KeyCombination ctrlZ = new KeyCodeCombination(Z, SHORTCUT_DOWN);
    final static KeyCombination ctrlY = new KeyCodeCombination(Y, SHORTCUT_DOWN);

    public static void addFocusListener(TextField tf, ProofView pv) {
        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            pv.lastFocusedTf = tf;
            pv.caretPosition = tf.getCaretPosition();
        });
    }
    public static void applyStyleIf(TextField expression, boolean bool, String style) {
        expression.getStyleClass().removeIf((s) -> s.equals(style));
        if (bool) {
            expression.getStyleClass().add(style);
        }
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
    public static void consumeKeys(TextField tf) {
        tf.setOnKeyPressed(key -> {
            if (ctrlY.match(key) || ctrlZ.match(key)) {
                key.consume();
            }
        });
    }
}
