package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.BoxReference;
import model.Proof;
import model.ProofListener;
import view.Command.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static view.ViewUtil.checkShortcut;

public class ProofView extends Symbolic implements ProofListener, View {
    ScrollPane sp;
    // TextFields of the premises and conclusion for quick access
    private TextField premises;
    private TextField conclusion;
    private Stack<VBox> curBoxDepth = new Stack<>();
    // This is a list of RowPanes, which are the "lines" of the proof.
    private List<RowPane> rList = new ArrayList<>();
    private List<Command> commandList = new LinkedList<Command>();
    private int curCommand = -1;
    private VBox lineNo;
    private VBox rows;
    //The tab object of this view
    private ViewTab tab;
    //The proof displayed in this view
    private Proof proof;
    //The patth where this proof was loaded/should be saved
    private String path;
    //Name of the proof/file of this view
    private String name;
    //hashmap for all the rules and number of arguments for all rules

    /**
     * Adds content to the TabPane in the proof and adds listeners to the premise and conclusion.
     * Vad är det som metoden gör med proof? lägg gärna till extra beskrivning om detta!!
     *
     * @param tabPane
     * @param premisesAndConclusion
     */

    public ProofView(TabPane tabPane, Proof proof, PremisesAndConclusion premisesAndConclusion) {
        this.proof = proof;
        this.proof.registerProofListener(this);
        this.premises = premisesAndConclusion.getPremises();
        this.premises.setId("expression");
        this.premises.setPromptText("Premise");
        premises.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                premises.setText(checkShortcut(newValue));
            }
        });
        this.conclusion = premisesAndConclusion.getConclusion();
        this.conclusion.setId("expression");
        this.conclusion.setPromptText("Conclusion");
        this.conclusion.textProperty().addListener((ov, oldValue, newValue) -> {
            newValue = checkShortcut(newValue);
            conclusion.setText(newValue);
            proof.updateConclusion(newValue);
        });
        proof.updateConclusion(this.conclusion.getText());

        this.premises.focusedProperty().addListener((observable, oldValue, newValue) -> {
            lastFocusedTf = this.premises;
            caretPosition = this.premises.getCaretPosition();
        });
        this.conclusion.focusedProperty().addListener((observable, oldValue, newValue) -> {
            lastFocusedTf = this.conclusion;
            caretPosition = this.conclusion.getCaretPosition();
        });
        AnchorPane cp = createProofPane();
        SplitPane sp = new SplitPane(premisesAndConclusion, cp);
        sp.setOrientation(Orientation.VERTICAL);
        sp.setDividerPosition(0, 0.1);
        AnchorPane anchorPane = new AnchorPane(sp);
        anchorPane.setTopAnchor(sp, 0.0);
        anchorPane.setRightAnchor(sp, 0.0);
        anchorPane.setLeftAnchor(sp, 0.0);
        anchorPane.setBottomAnchor(sp, 0.0);

        this.tab = new ViewTab("Proof", this);
        this.tab.setContent(anchorPane);
        tabPane.getTabs().add(this.tab);
        tabPane.getSelectionModel().select(this.tab); // Byt till den nya tabben
        if (proof.isLoaded == false) {
            newRow();
            --curCommand;
            commandList.clear();
        }
    }


    public ProofView(TabPane tabPane, Proof proof) {
        this(tabPane, proof, new PremisesAndConclusion());
    }

    public ProofView(TabPane tabPane, Proof proof, String sPremises, String sConclusion) {
        this(tabPane, proof, new PremisesAndConclusion(sPremises, sConclusion));
    }

    private AnchorPane createProofPane() {
        lineNo = new VBox();
        rows = new VBox();
        lineNo.setFillWidth(true);
        rows.setFillWidth(true);
        HBox hb = new HBox();
        hb.setHgrow(rows, Priority.ALWAYS);
        hb.getChildren().addAll(lineNo, rows);
        hb.setPadding(new Insets(5, 5, 5, 5));
        ScrollPane sp = new ScrollPane(hb);
        sp.getStyleClass().add("fit");
//		hb.heightProperty().addListener((ov, oldValue, newValue) -> {
//			if (newValue.doubleValue() > oldValue.doubleValue()) { // Change this to only trigger on new row!!
//				sp.setVvalue(1.0);                                 // Otherwise it will scroll down when you insert a row in the middle
//			}
//		});
        AnchorPane proofPane = new AnchorPane(sp);
        proofPane.setTopAnchor(sp, 0.0);
        proofPane.setRightAnchor(sp, 0.0);
        proofPane.setLeftAnchor(sp, 0.0);
        proofPane.setBottomAnchor(sp, 0.0);

        return proofPane;
    }


    /* Controller begin */
    private void executeCommand(Command c) {
        if (c.execute()) {
            ++curCommand;
            if (0 <= curCommand && curCommand < commandList.size())
                commandList.subList(curCommand, commandList.size()).clear();
            commandList.add(c);
        }
    }

    public void newRow() {
        executeCommand(new AddRow(proof, rList));
    }

    public void addRowAfterBox(int rowNo) {
        // Let's assume this is possible and check if it's the last row in a box or else we execute insertNewRow
        RowPane rp = rList.get(rowNo - 1);
        VBox parent = (VBox) rp.getParent();
        int idxOfRp = parent.getChildren().indexOf(rp);
        if (parent.getParent() instanceof VBox && idxOfRp == parent.getChildren().size() - 1)
            executeCommand(new AddRowAfterBox(proof, rowNo, rList));
        else
            executeCommand(new InsertRow(proof, rowNo, BoxReference.AFTER, rList));

    }

    public void insertNewRow(int rowNo, BoxReference br) {
        executeCommand(new InsertRow(proof, rowNo, br, rList));
    }

    public void insertNewBox(int rowNo) {
        executeCommand(new InsertBox(proof, rowNo, rList));
    }

    public void deleteRow(int rowNo) {
        RowPane rp = rList.get(rowNo - 1);
        VBox parent = (VBox) rp.getParent();
        if (parent.getParent() instanceof VBox && parent.getChildrenUnmodifiable().size() == 1) {
            executeCommand(new DeleteBox(proof, rowNo, rList));
            return;
        }
        executeCommand(new DeleteRow(proof, rowNo, rList));
    }

    public void undo() {
        if (0 <= curCommand && curCommand < commandList.size()) {
            commandList.get(curCommand).undo();
            --curCommand;
        }
    }

    public void redo() {
        if (0 <= curCommand + 1 && curCommand + 1 < commandList.size()) {
            commandList.get(curCommand + 1).execute();
            ++curCommand;
        }
    }
    /* End of controller */

    private void checkAndAdd(Region item) {
        if (curBoxDepth.isEmpty()) {
            rows.getChildren().add(item);
        } else {
            VBox temp = curBoxDepth.peek();
            temp.getChildren().add(item);
        }
    }

    /**
     * Creates a new row with a textfield for the expression and a textfield
     * for the rules and adds listeners to both of them.
     *
     * @return bp, the BorderPane containing two textfields.
     */


    //should only be called AFTER a new row has been added to rList since it uses rList.size()
    Label createLabel() {
        Label lbl = new Label("" + rList.size());
        lbl.getStyleClass().add("lineNo");
        lbl.setPadding(new Insets(8, 2, 2, 2));
        //lbl.setCache(true);
        //lbl.setCacheShape(true);
        lbl.setCacheHint(CacheHint.SPEED);
        return lbl;
    }


    // PROOF LISTENER METHODS
    //Adds a new row at the end of the proof
    public void rowAdded() {
        RowPane rp;
        if (curBoxDepth.isEmpty()) {
            rp = new RowPane(false, 0);
            rList.add(rp);
            rp.init(this, rList);
            rows.getChildren().add(rp);
        } else {
            VBox box = curBoxDepth.peek();
            List<Node> children = box.getChildren();
            boolean isFirstRowInBox = (children.isEmpty()) ? true : false;
            rp = new RowPane(isFirstRowInBox, 0);
            rList.add(rp);
            rp.init(this, rList);
            children.add(rp);
        }
        lineNo.getChildren().add(createLabel());
        updateLabelPaddings(rList.size());
    }

    public void rowInserted(int rowNo, BoxReference br, int depth) {
        RowPane referenceRow;
        boolean isFirstRowInBox;
        int nrOfClosingBoxes;
        int indexToInsertInParent;
        VBox parentBox;
        int rListInsertionIndex = (br == BoxReference.BEFORE) ? rowNo - 1 : rowNo;
        if (br == BoxReference.BEFORE) {
            referenceRow = rList.get(rowNo - 1);
            isFirstRowInBox = referenceRow.getIsFirstRowInBox();
            nrOfClosingBoxes = 0;
            referenceRow.setIsFirstRowInBox(false);
            parentBox = (VBox) referenceRow.getParent();
            indexToInsertInParent = parentBox.getChildren().indexOf(referenceRow);

        } else {
            referenceRow = rList.get(rowNo - 1);
            parentBox = (VBox) referenceRow.getParent();
            if (depth == 0) {
                nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
                isFirstRowInBox = false;
                referenceRow.setNrOfClosingBoxes(0);
                indexToInsertInParent = parentBox.getChildren().indexOf(referenceRow) + 1;
            } else {
                VBox childBox = parentBox;
                parentBox = (VBox) parentBox.getParent();
                for (int i = 1; i < depth; i++) {
                    childBox = parentBox;
                    parentBox = (VBox) parentBox.getParent();
                }
                isFirstRowInBox = false;
                nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes() - depth;
                referenceRow.setNrOfClosingBoxes(depth); // How many does this close?
                indexToInsertInParent = parentBox.getChildren().indexOf(childBox) + 1;
            }
        }
        RowPane rp = new RowPane(isFirstRowInBox, nrOfClosingBoxes);
        ((TextField) rp.getExpression()).setText("*");
        parentBox.getChildren().add(indexToInsertInParent, rp);
        rList.add(rListInsertionIndex, rp);
        rp.init(this, rList);
        lineNo.getChildren().add(createLabel());
        updateLabelPaddings(rowNo);
    }

    public void addedRowAfterBox(int rowNo) {
        RowPane referenceRow;
        referenceRow = rList.get(rowNo - 1); //Assume this is the last row in box
        VBox metaBox = (VBox) referenceRow.getParent().getParent();
        int nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
        referenceRow.setNrOfClosingBoxes(1);
        RowPane rp = new RowPane(false, nrOfClosingBoxes - 1);
        int idx = metaBox.getChildren().indexOf(referenceRow.getParent()) + 1;
        metaBox.getChildren().add(idx, rp);
        rList.add(rowNo, rp);
        rp.init(this, rList);
        lineNo.getChildren().add(createLabel());
        updateLabelPaddings(rowNo);
    }

    public void deletedRowAfterBox(int rowNo) {
        RowPane referenceRow;
        referenceRow = rList.get(rowNo - 1); //Assume this is the last row in box
        VBox metaBox = (VBox) referenceRow.getParent().getParent();
        int nrOfClosingBoxes = referenceRow.getNrOfClosingBoxes();
        int extraClosedBoxes = rList.get(rowNo).getNrOfClosingBoxes();
        referenceRow.setNrOfClosingBoxes(nrOfClosingBoxes + extraClosedBoxes);
        int idx = metaBox.getChildren().indexOf(referenceRow.getParent()) + 1;
        metaBox.getChildren().remove(idx);
        rList.remove(rowNo);
        List<Node> labelList = lineNo.getChildren();
        labelList.remove(labelList.size() - 1);
        updateLabelPaddings(rowNo - 1);
    }

    public void openBoxInView() {
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        checkAndAdd(vb);
        curBoxDepth.push(vb);
    }

    public void boxInserted(int rowNumber) {
        RowPane rp = rList.get(rowNumber - 1);
        VBox metabox = (VBox) rp.getParent();
        int indexOfRp = metabox.getChildren().indexOf(rp);
        VBox newBox = new VBox();
        newBox.getStyleClass().clear();
        newBox.getStyleClass().add("closedBox");
        newBox.getChildren().add(rp);
        metabox.getChildren().add(indexOfRp, newBox);
        rp.setIsFirstRowInBox(true);
        rp.incrementNrOfClosingBoxes();
        updateLabelPaddings(rowNumber);
    }

    public void boxRemoved(int rowNumber) {
        RowPane rp = rList.get(rowNumber - 1);
        rp.decrementNrOfClosingBoxes();
        VBox metabox = (VBox) rp.getParent().getParent();

        ObservableList<Node> children = metabox.getChildren();
        int idx = children.indexOf(rp.getParent());
        children.remove(idx);
        children.add(idx, rp);
        if (idx != 0) {
            rp.setIsFirstRowInBox(false);
        }
        updateLabelPaddings(rowNumber);
    }

    public void boxClosed() {
        if (!curBoxDepth.isEmpty()) {
            VBox vb = curBoxDepth.pop();

            //Update last row and its padding
            rList.get(rList.size() - 1).incrementNrOfClosingBoxes();
            updateLabelPaddings(rList.size() - 1);

            vb.getStyleClass().clear();
            vb.getStyleClass().add("closedBox");

        }
    }

    public void rowUpdated(String newText, boolean wellFormed, int lineNo) {
        TextField expression = (TextField) rList.get(lineNo - 1).getExpression();
        if (expression.getText().equals(""))
            wellFormed = true;
        if (newText != null)
            expression.setText(newText);
        else
            applyStyleIf(expression, !wellFormed, "bad");
    }

    public void conclusionReached(boolean correct, int lineNo) {
        //when a proof is loaded, the view has an empty rList
        if (rList.size() == 0) return;
        TextField expression = (TextField) rList.get(lineNo - 1).getExpression();
        applyStyleIf(expression, correct, "conclusionReached");
    }

    public void rowVerified(boolean verified, int lineNo) {
        TextField rule = (TextField) rList.get(lineNo - 1).getRule();
        applyStyleIf(rule, !verified, "unVerified");
    }

    //update view to reflect that row with nr rowNr has been deleted
    public void rowDeleted(int rowNr) {
        RowPane rp = rList.get(rowNr - 1);
        VBox box = (VBox) rp.getParent();
        List<Node> parentComponentList = box.getChildren();
        if (parentComponentList.remove(rp) == false) {
            System.out.println("ProofView.rowDeleted: something went wrong!");
            return;
        }
        rList.remove(rp);
        boolean wasOnlyRowInBox = parentComponentList.isEmpty();
        boolean updatePreviousRowLabel = false;

        //deleted row was last row in this box, remove the box
//

        if (rp.getIsFirstRowInBox()) { // next row is now first in this box
            RowPane nextRow = rList.get(rowNr - 1);
            nextRow.setIsFirstRowInBox(rp.getIsFirstRowInBox());
        }
        if (rp.getNrOfClosingBoxes() > 0) { // previous row now closes the boxes
            rList.get(rowNr - 2).setNrOfClosingBoxes(rp.getNrOfClosingBoxes());
            updatePreviousRowLabel = true;
        }
//        }

        //remove a Label and update paddings in relevant labels
        List<Node> labelList = lineNo.getChildren();
        labelList.remove(labelList.size() - 1);
        updateLabelPaddings(updatePreviousRowLabel ? rowNr - 1 : rowNr);
        int idxToFocus = rowNr - 1;
        if (idxToFocus >= rList.size()) {
            idxToFocus = rList.size() - 1;
        }
        RowPane focusThisPane = rList.get(idxToFocus);
        focusThisPane.getExpression().requestFocus();
    }
    // END OF PROOF LISTENER METHODS


    // public void focus() { // Save the last focused textfield here for quick resuming?
    //     Platform.runLater(() -> lastTf.requestFocus());
    // }
    private void applyStyleIf(TextField expression, boolean bool, String style) {
        expression.getStyleClass().removeIf((s) -> s.equals(style));
        if (bool) {
            expression.getStyleClass().add(style);
        }
    }

    public ViewTab getTab() {
        return tab;
    }

    public Proof getProof() {
        return proof;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //updates the padding for the labels starting from lineNr all the way down
    //TODO: get rid of magic numbers
    private void updateLabelPaddings(int lineNr) {
        List<Node> labelList = this.lineNo.getChildren();
        assert (rList.size() == labelList.size());
        //update the padding for each label by checking the corresponding rowPane
        for (int i = lineNr - 1; i < labelList.size(); i++) {
            RowPane rp = rList.get(i);
            int topPadding = rp.getIsFirstRowInBox() ? 11 : 8;
            int bottomPadding = 2 + 5 * rp.getNrOfClosingBoxes();
            Label label = (Label) labelList.get(i);
            label.setPadding(new Insets(topPadding, 2, bottomPadding, 2));
        }
    }

    //
    private void removeRecursivelyIfEmpty(VBox box) {
        Node parentNode = box.getParent();
        assert (box.getChildren().isEmpty());
        if (parentNode instanceof VBox) {
            VBox parentBox = (VBox) parentNode;
            parentBox.getChildren().remove(box);
            if (box.getStyleClass().toString().equals("openBox")) {
                VBox box2 = curBoxDepth.pop();
                assert (box == box2);
            }
            if (parentBox.getChildren().isEmpty()) {
                removeRecursivelyIfEmpty(parentBox);
            }
        }
    }

    /**
     * Adds the rule symbol that has been pressed to the text field for the rule when either "expression" or "righTextfield"
     * is focused.
     *
     * @param event
     */
    public void addRule(javafx.event.ActionEvent event) {
        addRule(event.toString().split("'")[1]);
    }

    public void addRule(String text) {
        if (lastFocusedTf.getId() == "rightTextfield") {
            int tmpCaretPosition = caretPosition;
            //String[] parts = event.toString().split("'");
            lastFocusedTf.setText(text);
            lastFocusedTf.requestFocus();
            lastFocusedTf.positionCaret(tmpCaretPosition + 1);
        } else if (lastFocusedTf.getId() == "expression") {
            TextField tmpLastFocusedTf = lastFocusedTf;
            BorderPane borderpane = (BorderPane) lastFocusedTf.getParent();
            RulePane rulepane = (RulePane) borderpane.getRight();
            TextField tf = (TextField) rulepane.getChildren().get(0);
            int tmpCaretPosition = caretPosition;
            //String[] parts = event.toString().split("'");
            tf.setText(text);
            tmpLastFocusedTf.requestFocus();
            tmpLastFocusedTf.positionCaret(tmpCaretPosition);
        }
    }

    /**
     * Returns the row index of the last focused textfield in the proof.
     *
     * @return row index of the last focused textfield in the proof, otherwise it returns -1
     */
    public int getRowNumberLastFocusedTF() {
        if (lastFocusedTf != null && rList.indexOf(lastFocusedTf.getParent()) != -1) {
            return rList.indexOf(lastFocusedTf.getParent()) + 1;
        }
        return -1;
    }


    /**
     * Display all the information in the loaded proof
     */
    public void displayLoadedProof() {
        List<RowInfo> proofInfo = proof.getProofInfo();

        for (int i = 1; i <= proofInfo.size(); i++) {
            RowInfo rowInfo = proofInfo.get(i - 1);
            System.out.println("i=" + i + " " + rowInfo);
            if (rowInfo.startBox && i != 1) {
                this.openBoxInView();
            }
            rowAdded();
            RowPane rowPane = this.rList.get(i - 1);


            rowPane.setExpression(rowInfo.expression);
            rowPane.setRule(rowInfo.rule);
            rowPane.setRulePrompt(0, rowInfo.ref1);
            rowPane.setRulePrompt(1, rowInfo.ref2);
            rowPane.setRulePrompt(2, rowInfo.ref3);

            if (rowInfo.endBox) {
                this.boxClosed();
            }
        }
        proof.verifyProof(0);
    }

    /**
     * Used in WelcomeView in order to update the rows with the premises
     *
     * @return a copy of rList
     */
    public List<RowPane> getRowList() {
        return new ArrayList<RowPane>(rList);
    }
}