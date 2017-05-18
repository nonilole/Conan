package view;

import javafx.scene.control.TextField;

public abstract class Symbolic {
	TextField lastFocusedTf;
	int caretPosition;
    /**
     * Adds the unicode symbol that has been pressed to the caret position in the focused text field.
     * @param event, the pressed unicode button.
     */
    public void addSymbol(javafx.event.ActionEvent event){
        if(lastFocusedTf != null && (lastFocusedTf.getId().equals("expression") || lastFocusedTf.getId().equals("rightTextField"))){
            int tmpCaretPosition = caretPosition;
            String[] parts = event.toString().split("'");
            lastFocusedTf.setText(lastFocusedTf.getText().substring(0, caretPosition) + parts[1]
                    + lastFocusedTf.getText().substring(caretPosition, lastFocusedTf.getLength()));
            lastFocusedTf.requestFocus();
            lastFocusedTf.positionCaret(tmpCaretPosition+1);
        }
    }
}
