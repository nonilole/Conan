package view;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.ProofListener;


public class ProofView implements ProofListener{
    static final int carryAddOpen = 3; // Magic
    static final int carryAddClose = 5; // Maybe have to calculate this from padding and font size?

    private TextField premises;
    private TextField conclusion;

    private Stack<VBox> curBoxDepth = new Stack<>();


    private List<BorderPane> rList = new LinkedList<>();
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
        if (!curBoxDepth.isEmpty()) {
            VBox vb = curBoxDepth.pop();
            vb.getStyleClass().clear();
            vb.getStyleClass().add("closedBox");
            carry += carryAddClose;

        }
    }


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

        rList.add(bp);


    }

    public void openBox() {
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        carry += carryAddOpen;
        checkAndAdd(vb);
        curBoxDepth.push(vb);

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

    /**Deletes the last row*/
    public void rowDeleteLastRow(){
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

        }


    }
    public void rowInserted(){}

}