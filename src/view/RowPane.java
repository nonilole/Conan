package view;

import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import model.BoxReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.*;
import static javafx.scene.input.KeyCombination.ModifierValue.*;
import static javafx.scene.input.KeyCombination.ModifierValue.DOWN;
import static view.ViewUtil.addFocusListener;
import static view.ViewUtil.checkShortcut;


public class RowPane extends BorderPane {
    final static KeyCombination shiftEnter = new KeyCodeCombination(ENTER, SHIFT_DOWN);
    final static KeyCombination ctrlLeft = new KeyCodeCombination(LEFT, SHORTCUT_DOWN);
    final static KeyCombination ctrlRight = new KeyCodeCombination(RIGHT, SHORTCUT_DOWN);
    final static KeyCombination ctrlB = new KeyCodeCombination(B, SHORTCUT_DOWN);
    final static KeyCombination ctrlD = new KeyCodeCombination(D, SHORTCUT_DOWN);
    final static KeyCombination ctrlZ = new KeyCodeCombination(Z, SHORTCUT_DOWN);
    final static KeyCombination ctrlShiftZ = new KeyCodeCombination(Z, SHORTCUT_DOWN, SHIFT_DOWN);
    final static KeyCombination ctrlY = new KeyCodeCombination(Y, SHORTCUT_DOWN);
    private static final HashMap<String, List<Boolean>> ruleBox;
    private static final HashMap<String, Integer> ruleMap;
    static Pattern p = Pattern.compile("^([1-9]\\d*-?([1-9]\\d*)?)?$");

    static {
        List<Boolean> ft = Arrays.asList(false, true, true);
        List<Boolean> tf = Arrays.asList(true, false, true);
        ruleBox = new HashMap<String, List<Boolean>>();
        ruleBox.put("∨E", ft);
        ruleBox.put("→I", tf);
        ruleBox.put("¬I", tf);
        ruleBox.put("∀I", tf);
        ruleBox.put("∃E", ft);
        ruleBox.put("=I", ft);
        ruleBox.put("MT", tf);
        ruleBox.put("PBC", tf);
    }

    static {
        ruleMap = new HashMap<String, Integer>();
        ruleMap.put("∧I", 2);
        ruleMap.put("∧E1", 1);
        ruleMap.put("∧E2", 1);
        ruleMap.put("∨I1", 1);
        ruleMap.put("∨I2", 1);
        ruleMap.put("∨E", 3);
        ruleMap.put("→I", 1);
        ruleMap.put("→E", 2);
        ruleMap.put("⊥E", 1);
        ruleMap.put("¬I", 1);
        ruleMap.put("¬E", 2);
        ruleMap.put("¬¬E", 1);
        ruleMap.put("Premise", 0);
        ruleMap.put("∀I", 1);
        ruleMap.put("∀E", 1);
        ruleMap.put("∃I", 1);
        ruleMap.put("∃E", 2);
        ruleMap.put("=E", 2);
        ruleMap.put("=I", 0);
        ruleMap.put("Ass.", 0);
        ruleMap.put("Fresh", 0);
        ruleMap.put("MT", 2);
        ruleMap.put("LEM", 0);
        ruleMap.put("PBC", 1);
        ruleMap.put("¬¬I", 1);
        ruleMap.put("Copy", 1);
    }

    private int numberOfPrompts;
    private boolean isFirstRowInBox;
    private int nrOfClosingBoxes;
    private RulePane rulePane;

    // Always call init after adding RowPane to rList
    public RowPane(boolean isFirstRowInBox, int nrOfClosingBoxes) {
        super();
        this.isFirstRowInBox = isFirstRowInBox;
        this.setNrOfClosingBoxes(nrOfClosingBoxes);
        this.numberOfPrompts = 0;
        TextField tfExpression = new TextField();
        tfExpression.setPromptText("Expression");
        tfExpression.setId("expression");
        tfExpression.getStyleClass().add("myText");
        tfExpression.setPrefWidth(580);
        this.setCenter(tfExpression);
        rulePane = new RulePane();
        this.setRight(rulePane);
        this.setCache(true);
        this.setCacheShape(true);
        this.setCacheHint(CacheHint.DEFAULT);
    }

    public void init(ProofView pv, List<RowPane> rList) {
        setListeners(pv, rList);
        setMenu(pv, rList);
    }

    private void setMenu(ProofView pv, List<RowPane> rList) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem insertAbove = new MenuItem("Insert above");
        MenuItem insertHere = new MenuItem("Insert here");
        MenuItem insertBelow = new MenuItem("Insert below");
        MenuItem insertBox = new MenuItem("Insert box");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        contextMenu.getItems().add(insertAbove);
        contextMenu.getItems().add(insertHere);
        contextMenu.getItems().add(insertBelow);
        contextMenu.getItems().add(insertBox);
        for (int i = 0; i < 3; i++) {
            getRulePrompt(i).setContextMenu(contextMenu);
        }
        getExpression().setContextMenu(contextMenu);
        getRule().setContextMenu(contextMenu);


        delete.setOnAction(event -> {
            int rowOfPressedButton = rList.indexOf(this) + 1;
            pv.deleteRow(rowOfPressedButton);
        });
        insertAbove.setOnAction(event -> {
            int rowOfPressedButton = rList.indexOf(this) + 1;
            pv.insertNewRow(rowOfPressedButton, BoxReference.BEFORE);
        });
        insertHere.setOnAction(event -> {
            int rowOfPressedButton = rList.indexOf(this) + 1;
            pv.insertNewRow(rowOfPressedButton, BoxReference.AFTER);
        });
        insertBelow.setOnAction(event -> {
            int rowOfPressedButton = rList.indexOf(this) + 1;
            pv.addRowAfterBox(rowOfPressedButton);
        });
        insertBox.setOnAction(event -> {
            int rowOfPressedButton = rList.indexOf(this) + 1;
            pv.insertNewBox(rowOfPressedButton);
        });
    }

    private void setListeners(ProofView pv, List<RowPane> rList) {
        new ExpressionFocus(getExpression(), pv, rList);
        getExpression().textProperty().addListener((ov, oldValue, newValue) -> {
            newValue = checkShortcut(newValue);
            setExpression(newValue);
            int rpIndex = rList.indexOf(this);
            pv.getProof().updateFormulaRow(newValue, rpIndex + 1);
        });
        new RuleFocus(getRule(), pv, rList);
        getRule().textProperty().addListener((ov, oldValue, newValue) -> {
            newValue = checkShortcut(newValue);
            setRule(newValue);
            int rpIndex = rList.indexOf(this);
            if (newValue.equals("Ass.") || newValue.equals("Fresh")) {
                pv.insertNewBox(rpIndex + 1);
            }
            try {
                pv.getProof().updateRuleRow(newValue, rpIndex + 1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            setPrompts(ruleMap.getOrDefault(newValue, 0));
            setPromptsPromptText(ruleBox.getOrDefault(newValue, Arrays.asList(false, false, true)));
        });
        for (int i = 0; i < 3; i++) {
            new PromptFocus(getRulePrompt(i), pv, rList, i);
            setPromptListener(getRulePrompt(i), pv, rList, i);
        }
    }

    private void setPromptListener(TextField prompt, ProofView pv, List<RowPane> rList, int promptIndex) {
        prompt.textProperty().addListener((observable, oldValue, newValue) -> {
            Matcher m = p.matcher(newValue);
            if (m.matches()) {
                int rpNr = rList.indexOf(this) + 1;
                pv.getProof().rulePromptUpdate(rpNr, promptIndex + 1, newValue);
            } else {
                prompt.setText(oldValue);

            }
        });
    }

    public boolean getIsFirstRowInBox() {
        return isFirstRowInBox;
    }

    public void setIsFirstRowInBox(boolean b) {
        isFirstRowInBox = b;
    }

    public int getNrOfClosingBoxes() {
        return nrOfClosingBoxes;
    }

    public void setNrOfClosingBoxes(int nrOfClosingBoxes) {
        this.nrOfClosingBoxes = nrOfClosingBoxes;
    }

    public void incrementNrOfClosingBoxes() {
        nrOfClosingBoxes++;
    }

    public void decrementNrOfClosingBoxes() {
        nrOfClosingBoxes--;
    }

    public TextField getExpression() {
        return (TextField) this.getCenter();
    }

    public void setExpression(String s) {
        getExpression().setText(s);
    }

    public TextField getRule() {
        return rulePane.getRule();
    }

    public void setRule(String s) {
        getRule().setText(s);
    }

    private TextField getClosestPromptFromLeft(int index) {
        System.out.println(index);
        if (index >= this.numberOfPrompts)
            index = this.numberOfPrompts - 1;
        if (index == -1)
            return getRule();
        return getRulePrompt(index);
    }

    public void setRulePrompt(int i, String s) {
        getRulePrompt(i).setText(s);
    }

    public TextField getRulePrompt(int index) {
        return rulePane.getRulePrompt(index);
    }

    private void hideAndClearPrompts() {
        for (int i = 0; i < 3; i++) {
            getRulePrompt(i).setVisible(false);
            getRulePrompt(i).clear();
        }
    }

    private void setPromptsPromptText(List<Boolean> isBoxRef) {
        for (int i = 0; i < 3; i++) {
            if (isBoxRef.get(i).equals(true))
                getRulePrompt(i).setPromptText("123-456");
            else
                getRulePrompt(i).setPromptText("Row");
        }
    }

    private void setPrompts(int n) {
        hideAndClearPrompts();
        this.numberOfPrompts = n;
        switch (n) {
            case 3:
                getRulePrompt(2).setVisible(true);
            case 2:
                getRulePrompt(1).setVisible(true);
            case 1:
                getRulePrompt(0).setVisible(true);
                break;
            default:
                break;
        }
    }

    private abstract class HotkeyMapper {
        private HotkeyMapper(TextField trigger, ProofView pv, List<RowPane> rList) {
            addFocusListener(trigger, pv);
            trigger.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent key) {
                    int index = rList.indexOf(RowPane.this);
                     if (ctrlY.match(key) || ctrlShiftZ.match(key)) {
                         pv.redo();
                         key.consume();
                     } else if (ctrlZ.match(key)) {
                        pv.undo();
                        key.consume();
                    } else if (ctrlD.match(key)) {
                        pv.deleteRow(index + 1);
                    } else if (ctrlB.match(key)) {
                        pv.insertNewBox(index + 1);
                    } else if (ctrlLeft.match(key)) {
                        focusLeft();
                    } else if (ctrlRight.match(key)) {
                        focusRight();
                    } else if (shiftEnter.match(key)) {
                        pv.addRowAfterBox(index + 1);
                        focus(pv, rList, index + 1);
                    } else if (key.getCode() == ENTER) {
                        pv.insertNewRow(index + 1, BoxReference.AFTER);
                        focus(pv, rList, index + 1);
                    }  else if (key.getCode() == KeyCode.DOWN) {
                        if (index + 1 >= rList.size())
                            pv.addRowAfterBox(index + 1);
                        focus(pv, rList, index + 1);
                    } else if (key.getCode() == KeyCode.UP) {
                        if (index - 1 >= 0) {
                            focus(pv, rList, index - 1);
                        }
                    }
                }
            });
        }

        public abstract void focus(ProofView pv, List<RowPane> rList, int index);

        public abstract void focusLeft();

        public abstract void focusRight();
    }

    private class RuleFocus extends HotkeyMapper {
        private RuleFocus(TextField trigger, ProofView pv, List<RowPane> rList) {
            super(trigger, pv, rList);
        }

        @Override
        public void focus(ProofView pv, List<RowPane> rList, int index) {
            rList.get(index).getRule().requestFocus();
        }

        @Override
        public void focusLeft() {
            getExpression().requestFocus();
        }

        @Override
        public void focusRight() {
            getClosestPromptFromLeft(0).requestFocus();
        }
    }

    private class ExpressionFocus extends HotkeyMapper {
        public ExpressionFocus(TextField trigger, ProofView pv, List<RowPane> rList) {
            super(trigger, pv, rList);
        }

        @Override
        public void focus(ProofView pv, List<RowPane> rList, int index) {
            rList.get(index).getExpression().requestFocus();
        }

        @Override
        public void focusLeft() {
        }

        @Override
        public void focusRight() {
            getRule().requestFocus();
        }
    }

    private class PromptFocus extends HotkeyMapper {
        int prompt;

        private PromptFocus(TextField trigger, ProofView pv, List<RowPane> rList, int prompt) {
            super(trigger, pv, rList);
            this.prompt = prompt;
        }

        @Override
        public void focus(ProofView pv, List<RowPane> rList, int index) {
            rList.get(index).getClosestPromptFromLeft(prompt).requestFocus();
        }

        @Override
        public void focusLeft() {
            if (prompt == 0) {
                getRule().requestFocus();
            } else {
                getClosestPromptFromLeft(prompt - 1).requestFocus();
            }

        }

        @Override
        public void focusRight() {
            getRulePrompt(prompt + 1).requestFocus();
        }
    }
}
