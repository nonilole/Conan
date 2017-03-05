package view;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Proof;
import model.ProofListener;


/***
 * The ProofView panes consists of two VBoxes
 *
 * One for lineNumbers and one for the rows with TextFields.
 *
 * LineNo | rows
 * -------------------
 * 1      | BorderPane
 * 2      | BorderPane
 * 3      | BorderPane
 * 4      | BorderPane
 *
 * The row is a BorderPane and consists of two TextFields
 *
 * Row
 * =============
 * |center|right
 *
 * These can be reached by calling the BorderPanes method's getCenter() and getRight().
 * Remember to cast these to TextFields.
 * E.g. (TextField) rList.get(rList.size()-1).getCenter()
 *
 * Keep in mind, that each child of rows is not a BorderPane. Boxes are additional VBoxes, with styling.
 * E.g.
 * rows
 * ====
 * Box
 *      BorderPane
 *      BorderPane
 *      Box
 *          BorderPane
 *      BorderPane
 * BorderPane
 */
public class ProofView implements ProofListener, View{
    /*
     * These are magic constants that decide the lineNo padding.
     * Margin can't be changed as a property, so the solution is to take into account how much the border
     * and the padding increases the distance between rows and add the padding to the line numbers accordingly.
     */
    static final int carryAddOpen = 3;
    static final int carryAddClose = 5;


    // TextFields of the premises and conclusion for quick access
    private TextField premises;
    private TextField conclusion;

    private Stack<VBox> curBoxDepth = new Stack<>();

    // This is a list of BorderPanes, which are the "lines" of the proof.
    private List<BorderPane> rList = new LinkedList<>();
    private int counter = 1;
    private int carry = 0;


    private VBox lineNo;
    private VBox rows;

    //The tab object of this view
    private Tab tab;

    //The proof displayed in this view
    private Proof proof;

    //The patth where this proof was loaded/should be saved
    private String path;

    //Name of the proof/file of this view
    private String name;

    private TextField lastTf;

    /**
     * This ia listener that is applied to the last textField. It creates a new row, each time the value of the textField is changed.
     */
    private ChangeListener<? extends String> lastTfListener = (ov, oldValue, newValue) -> {
        newRow();
    };

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
        hb.heightProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.doubleValue() > oldValue.doubleValue()) { // Change this to only trigger on new row!!
                sp.setVvalue(1.0);                                 // Otherwise it will scroll down when you insert a row in the middle
            }
        });
        AnchorPane proofPane = new AnchorPane(sp);
        proofPane.setTopAnchor(sp, 0.0);
        proofPane.setRightAnchor(sp, 0.0);
        proofPane.setLeftAnchor(sp, 0.0);
        proofPane.setBottomAnchor(sp, 0.0);

        return proofPane;
    }

    public ProofView(TabPane tabPane, Proof proof, HBox premisesAndConclusion) {
        this.proof = proof;
        this.proof.registerProofListener(this);
        this.premises = (TextField) premisesAndConclusion.getChildren().get(0);
        this.conclusion = (TextField) premisesAndConclusion.getChildren().get(2);
        SplitPane sp = new SplitPane(premisesAndConclusion, createProofPane());
        sp.setOrientation(Orientation.VERTICAL);
        sp.setDividerPosition(0, 0.1);
        AnchorPane anchorPane = new AnchorPane(sp);
        anchorPane.setTopAnchor(sp, 0.0);
        anchorPane.setRightAnchor(sp, 0.0);
        anchorPane.setLeftAnchor(sp, 0.0);
        anchorPane.setBottomAnchor(sp, 0.0);
        ProofTab tab = new ProofTab("Proof", this);
        this.tab = tab;
        tab.setContent(anchorPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab); // Byt till den nya tabben
        newRow();
    }
    public ProofView(TabPane tabPane, Proof proof) {
        this(tabPane, proof, CommonPanes.premisesAndConclusion());
    }
    public ProofView(TabPane tabPane, Proof proof, String sPremises, String sConclusion) {
        this(tabPane, proof, CommonPanes.premisesAndConclusion(sPremises, sConclusion));
    }


    /* Controller begin */
    public void openBox() {
        proof.openBox();
    }
    public void closeBox() {
        proof.closeBox();
    }
    public void newRow() {
        proof.addRow("", "");
    }
    public void rowDeleteLastRow(){
        proof.deleteRow();
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
    private BorderPane createRow() {
        BorderPane bp = new BorderPane();
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        tf1.getStyleClass().add("myText");
        tf2.getStyleClass().add("myText");
        bp.setCenter(tf1);
        bp.setRight(tf2);

        return bp;
    }
    Label createLabel() {
        Label lbl = new Label(""+counter++);
        lbl.getStyleClass().add("lineNo");
        lbl.setPadding(new Insets(8+carry,2,2,2));

        carry = 0;
        return lbl;
    }


    public void rowInserted() {
        BorderPane bp = createRow();
        checkAndAdd(bp);
        int curRowNo = counter;
        lineNo.getChildren().add(createLabel());
        if (lastTf != null) {
            lastTf.textProperty().removeListener((ChangeListener<? super String>) lastTfListener);
        }
        TextField tempTf = (TextField) bp.getCenter();
        lastTf = tempTf;
        lastTf.textProperty().addListener((ChangeListener<? super String>) lastTfListener);
        // Updates the Proof object if the textField is updated
        lastTf.textProperty().addListener((ov, oldValue, newValue) -> {
            proof.updateFormulaRow(newValue, curRowNo);
        });
        TextField rule = (TextField) bp.getRight();
        // Updates the Proof object if the textField is updated
        rule.textProperty().addListener((ov, oldValue, newValue) -> {
            proof.updateRuleRow(newValue, curRowNo);
        });
        rList.add(bp);
    }


   // public void focus() { // Save the last focused textfield here for quick resuming?
   //     Platform.runLater(() -> lastTf.requestFocus());
   // }



    public void boxOpened(){
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        carry += carryAddOpen;
        checkAndAdd(vb);
        curBoxDepth.push(vb);
        newRow();
    }
    public void boxClosed(){
        if (!curBoxDepth.isEmpty()) {
            VBox vb = curBoxDepth.pop();
            vb.getStyleClass().clear();
            vb.getStyleClass().add("closedBox");
            carry += carryAddClose;

        }
    }
    public void rowUpdated(boolean wellFormed, int lineNo) {
        TextField expression = (TextField) rList.get(lineNo-1).getCenter();
        if (wellFormed) {
            expression.getStyleClass().removeIf((s) -> s.equals("bad")); // Remove the bad-class from textField.
        } else {
            expression.getStyleClass().add("bad");
        }
    }
    public void conclusionReached(){}

    /**Deletes the last row*/
    public void rowDeleted(){
        //todo make sure that the lines gets updated correctly + refactor the code

        int lastRow=rList.size()-1;
        //Do nothing when no row is present
        if(rList.size()==0){

        }
        //delete the closing part of the box
        else if(rList.get(rList.size()-1).getParent().getStyleClass().toString().equals("closedBox")){
            VBox node=(VBox)rList.get(rList.size()-1).getParent();
            //pushes the box for the last row
            curBoxDepth.push(node);

            //used for pushing back the closed boxes
            ArrayList<VBox>v=new ArrayList<>();
            v.add(node);

            //Remove all the closing part of boxes that encloses the last row
            while(node.getParent() instanceof VBox){

                if(node.getStyleClass().toString().equals("closedBox"))
                {
                    carry-=carryAddClose;
                }

                node.getStyleClass().clear();
                node.getStyleClass().add("openBox");

                node = (VBox) node.getParent();

                v.add(node);


            }

            //pushes back the closed boxes to the stack in reverse
            for(int i=0;i<v.size();i++){
                curBoxDepth.push(v.get((v.size()-1)-i));
            }

        }
        //delete open part of the box
        else if(rList.get(lastRow).getParent().getChildrenUnmodifiable().size()==1 &&
                rList.get(lastRow).getParent().getStyleClass().toString().equals("openBox")){

;

            VBox grandParentBox=((VBox)rList.get(rList.size()-1).getParent().getParent());
            grandParentBox.getChildren().remove(grandParentBox.getChildren().size()-1);
            rList.remove(rList.size()-1);
            lineNo.getChildren().remove(lineNo.getChildren().size() - 1);
            counter--;
            if (!curBoxDepth.isEmpty()){
                curBoxDepth.pop();
            }

        }
        //delete row
        else if(rList.get(lastRow).getParent().getChildrenUnmodifiable().size()>0){
            ((VBox)rList.get(lastRow).getParent()).getChildren().remove(((VBox) rList.get(lastRow).getParent()).getChildren().size()-1);
            rList.remove(rList.size()-1);
            counter--;
            lineNo.getChildren().remove(lineNo.getChildren().size() - 1);
            BorderPane lastBP = rList.get(rList.size()-1);
            lastTf = (TextField) lastBP.getCenter();
            lastTf.textProperty().addListener((ChangeListener<? super String>) lastTfListener);
        }


    }
    public Tab getTab(){ return tab;}
    public Proof getProof(){ return proof;}
    public String getPath(){ return path;}
    public void setPath(String path){ this.path = path; }
    public String getName(){ return name;}
    public void setName(String name){ this.name = name; }
}