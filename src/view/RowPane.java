package view;

import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.BoxReference;
import start.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;
import static view.ViewUtil.addFocusListener;
import static view.ViewUtil.checkShortcut;


public class RowPane extends BorderPane {
    final static KeyCombination ctrlLeft = new KeyCodeCombination(LEFT, SHORTCUT_DOWN);
    final static KeyCombination ctrlRight = new KeyCodeCombination(RIGHT, SHORTCUT_DOWN);
    final static KeyCombination ctrlZ = new KeyCodeCombination(Z, SHORTCUT_DOWN);
    final static KeyCombination ctrlShiftZ = new KeyCodeCombination(Z, SHORTCUT_DOWN, SHIFT_DOWN);
    final static KeyCombination ctrlY = new KeyCodeCombination(Y, SHORTCUT_DOWN);
    private static final HashMap<String, List<Boolean>> ruleBox;
    private static final HashMap<String, Integer> ruleMap;
    private String errorStatus;
    private String parsingStatus;
    static Pattern p = Pattern.compile("^(([1-9]\\d*)?-?([1-9]\\d*)?)?$");


    static {
        List<Boolean> ft = Arrays.asList(false, true, true);
        List<Boolean> tf = Arrays.asList(true, false, true);
        ruleBox = new HashMap<>();
        ruleBox.put(Constants.disjunctionElim, ft);
        ruleBox.put(Constants.implicationIntro, tf);
        ruleBox.put(Constants.negationIntro, tf);
        ruleBox.put(Constants.forallIntro, tf);
        ruleBox.put(Constants.existsElim, ft);
        ruleBox.put(Constants.equalityIntro, ft);
        ruleBox.put(Constants.proofByContradiction, tf);
    }

    static {
        ruleMap = new HashMap<String, Integer>();
        ruleMap.put(Constants.conjunctionIntro, 2);
        ruleMap.put(Constants.conjunctionElim1, 1);
        ruleMap.put(Constants.conjunctionElim2, 1);
        ruleMap.put(Constants.disjunctionIntro1, 1);
        ruleMap.put(Constants.disjunctionIntro2, 1);
        ruleMap.put(Constants.disjunctionElim, 3);
        ruleMap.put(Constants.implicationIntro, 1);
        ruleMap.put(Constants.implicationElim, 2);
        ruleMap.put(Constants.contradictionElim, 1);
        ruleMap.put(Constants.negationIntro, 1);
        ruleMap.put(Constants.negationElim, 2);
        ruleMap.put(Constants.doubleNegationElim, 1);
        ruleMap.put(Constants.premise, 0);
        ruleMap.put(Constants.forallIntro, 1);
        ruleMap.put(Constants.forallElim, 1);
        ruleMap.put(Constants.existsIntro, 1);
        ruleMap.put(Constants.existsElim, 2);
        ruleMap.put(Constants.equalityElim, 2);
        ruleMap.put(Constants.equalityIntro, 0);
        ruleMap.put(Constants.assumption, 0);
        ruleMap.put(Constants.freshVar, 0);
        ruleMap.put(Constants.modusTollens, 2);
        ruleMap.put(Constants.lawOfExcludedMiddle, 0);
        ruleMap.put(Constants.proofByContradiction, 1);
        ruleMap.put(Constants.doubleNegationIntro, 1);
        ruleMap.put(Constants.copy, 1);
    }

    private int numberOfPrompts;
    private boolean isFirstRowInBox;
    private int nrOfClosingBoxes;

    public void setErrorStatus(String s) {
        this.errorStatus = s;
    }
    public String getErrorStatus() {
        return this.errorStatus;
    }
    public void setParsingStatus(String s) {
        this.parsingStatus = s;
    }
    public String getParsingStatus() {return this.parsingStatus;}


        // Always call init after adding RowPane to rList
    public RowPane(boolean isFirstRowInBox, int nrOfClosingBoxes) {
        super();
        this.isFirstRowInBox = isFirstRowInBox;
        this.setNrOfClosingBoxes(nrOfClosingBoxes);
        this.numberOfPrompts = 0;
        TextField tfExpression = new TextField();
        tfExpression.setPromptText("Formula");
        tfExpression.setId("expression");
        tfExpression.getStyleClass().add("myText");
        tfExpression.setPrefWidth(580);
        this.setCenter(tfExpression);
        this.setRight(new RulePane());
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
        MenuItem insertAbove = new MenuItem("Insert row above");
        MenuItem insertHere = new MenuItem("Insert row (Enter)");
        MenuItem insertBelow = new MenuItem("Insert row below current box (Shift+Enter)");
        MenuItem insertBox = new MenuItem("Open box (CTRL+B)");
        MenuItem delete = new MenuItem("Delete current row or box (CTRL+D)");
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
            String changedValue = checkShortcut(newValue, true);
            if (!changedValue.equals(newValue)) {
                setRule(changedValue);
                return;
            }
            int rpIndex = rList.indexOf(this);
//            if (newValue.equals("Ass.") || newValue.equals("Fresh")) {
//                pv.insertNewBox(rpIndex + 1);
//            }
            try {
                pv.getProof().updateRuleRow(newValue, rpIndex + 1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            newValue = newValue.replaceAll("^(∃|∀)[a-z](i|e)$", "$1$2");
            setPrompts(ruleMap.getOrDefault(newValue, -1));
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
        return (TextField) ((FlowPane) this.getRight()).getChildren().get(0);
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
        return (TextField) ((FlowPane) this.getRight()).getChildren().get(1+index);
    }

    private void hideAndClearPrompts() {
        for (int i = 0; i < 3; i++) {
            getRulePrompt(i).setVisible(false);
            getRulePrompt(i).clear();
        }
    }

    private void setPromptsPromptText(List<Boolean> isBoxRef) {
        for (int i = 0; i < 3; i++) {
            if (isBoxRef.get(i))
                getRulePrompt(i).setPromptText(Constants.intervalPromptText);
            else
                getRulePrompt(i).setPromptText(Constants.rowPromptText);
        }
    }

    public int getNumberOfPrompts() {
        return this.numberOfPrompts;
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
            default:
                break;
        }
        if (n > 0) {
            getClosestPromptFromLeft(0).requestFocus();
        }
      //if the rule has rule promps
        /*
        if (n > 0) {
            System.out.println(n);
            Preferences prefs = Preferences.userRoot().node("General");
            //if generate is checked and if popup has not been disabled. 
            if (prefs.getBoolean("generate", true) && prefs.getBoolean("generateHelp", true)) { 
                Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
                popup.setDialogPane(new DialogPane() {
                    @Override
                    protected Node createDetailsButton() {
                        CheckBox doNotShowMeThis = new CheckBox();
                        doNotShowMeThis.setSelected(true);
                        doNotShowMeThis.setText("Please stop annoying me.");
                        doNotShowMeThis.setOnAction(e -> {
                            if (doNotShowMeThis.isSelected()) {
                                prefs.putBoolean("generateHelp", false);
                            } else {
                                prefs.putBoolean("generateHelp", true);
                            }
                        });
                        return doNotShowMeThis;
                    }
                });
                popup.setTitle("Generate");
                popup.setHeaderText("Generating expressions");
                popup.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                popup.getDialogPane().setContentText("You may generate an expression by leaving the expression field empty and inputting valid and verified references.\n\n"
                + Constants.forallIntro + " may generate if you type the variable name between " + Constants.forall + " and " + Constants.introduction + ". E.g." + Constants.forall + "x" + Constants.introduction +"\n\n"
                + "The rules " + Constants.existsIntro + ", " + Constants.equalityIntro + " and " + Constants.equalityElim + " may not generate\n\n"
                );
                popup.getDialogPane().setExpandableContent(new Group());
                popup.getDialogPane().setExpanded(true);
                popup.show();
            }
        }
       */ 
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
                    } else if (ctrlLeft.match(key)) {
                        focusLeft();
                    } else if (ctrlRight.match(key)) {
                        focusRight();
                    } else if (key.getCode() == KeyCode.DOWN) {
                        if (index + 1 >= rList.size())
                            pv.addRowAfterBox(index + 1);
                        focus(pv, rList, index + 1);
                    } else if (key.getCode() == KeyCode.UP) {
                        if (index - 1 >= 0) {
                            focus(pv, rList, index - 1);
                        } else {
                            pv.insertNewRow(0, BoxReference.AFTER);
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
