package view;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.ProofListener;
import model.ProofTab;


public class ProofView implements ProofListener{
    static final int carryAddOpen = 3; // Magic
    static final int carryAddClose = 5; // Maybe have to calculate this from padding and font size?

    private TextField premises;
    private TextField conclusion;

    private Stack<VBox> stack = new Stack<>();


    private List<BorderPane> rList = new LinkedList<>();
    private List<VBox> rowList = new LinkedList<VBox>();
    private List<VBox> lineNoList = new ArrayList<VBox>();
    int index=0;
    private int counter = 1;
    private int carry = 0;


    private ScrollPane scrollPane;
    private VBox lineNo;
    private VBox rows;

    private TextField lastTf;
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
        scrollPane = sp;
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
        rowList.add(rows);
        lineNoList.add(lineNo);

        return proofPane;
    }

    public ProofView(TabPane tabPane, HBox premisesAndConclusion) {
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
        tab.setContent(anchorPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab); // Byt till den nya tabben
        newRow();
    }
    public ProofView(TabPane tabPane) {
        this(tabPane, CommonPanes.premisesAndConclusion());
    }
    public ProofView(TabPane tabPane, String sPremises, String sConclusion) {
        this(tabPane, CommonPanes.premisesAndConclusion(sPremises, sConclusion));
    }


    public void closeBox() {
        if (!stack.isEmpty()) {
            VBox vb = stack.pop();
            vb.getStyleClass().clear();
            vb.getStyleClass().add("closedBox");
            carry += carryAddClose;
            index--;
        }
    }


    private void checkAndAdd(Region item) {
        if (stack.isEmpty()) {
            rows.getChildren().add(item);
        } else {
            VBox temp = stack.peek();
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

    public void newRow() {
        BorderPane bp = createRow();
        checkAndAdd(bp);
        BorderPane tf = bp;
        lineNo.getChildren().add(createLabel());
        if (lastTf != null) {
            lastTf.textProperty().removeListener((ChangeListener<? super String>) lastTfListener);
        }
        TextField tempTf = (TextField) bp.getCenter();
        lastTf = tempTf;
        lastTf.textProperty().addListener((ChangeListener<? super String>) lastTfListener);


    }

    public void openBox() {
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        carry += carryAddOpen;
        checkAndAdd(vb);
        stack.push(vb);
        rowList.add(rows);
        lineNoList.add(lineNo);
        index++;
        newRow();
    }

   // public void focus() { // Save the last focused textfield here for quick resuming?
   //     Platform.runLater(() -> lastTf.requestFocus());
   // }



    public void boxOpened(){}
    public void boxClosed(){}
    public void rowUpdated(){}
    public void conclusionReached(){}
    public void rowDeleted(){}


    public void rowDeleteLastRow(){
        int lastRow=rowList.get(0).getChildren().size()-1;
        Pane node=null;
        if(lastRow!=-1)
        {
            if (rowList.get(0).getChildren().get(lastRow) instanceof BorderPane) {
                rowList.get(0).getChildren().remove(lastRow);
                lineNo.getChildren().remove(lineNo.getChildren().size() - 1);
                counter--;
            } else if (rowList.get(0).getChildren().get(lastRow) instanceof VBox) {
                node = (VBox) rowList.get(0).getChildren().get(lastRow);
            }
            //Go deeper when encountering a VBox
            while (node instanceof VBox) {
                lastRow = node.getChildren().size() - 1;
                //The case when only the open box is left
                if (node.getChildren().size() == 0) {
                    int last = ((VBox) node.getParent()).getChildren().size() - 1;
                    ((VBox) node.getParent()).getChildren().remove(last);
                    //carry -= carryAddOpen;
                    if (!stack.isEmpty())
                        stack.pop();
                    break;
                }
                //Deletes the row
                else if (node.getChildren().get(lastRow) instanceof BorderPane) {
                    node.getChildren().remove(lastRow);
                    lineNo.getChildren().remove(lineNo.getChildren().size() - 1);
                    counter--;
                    break;
                }

                //Traverse to the final line
                node = (VBox) node.getChildren().get(lastRow);

            }


            //delete closed boxes
            if (node != null && node.getStyleClass().toString().equals("closedBox")) {
                VBox vb = (VBox) node;
                vb.getStyleClass().clear();
                vb.getStyleClass().add("openBox");
                stack.push(vb);
                carry -= carryAddClose;
            }

        }
    }
    public void rowInserted(){}

}