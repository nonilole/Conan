package view;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.ProofListener;
import model.ProofTab;


public class ProofView implements ProofListener{
    static final int carryAddOpen = 3; // Magic
    static final int carryAddClose = 5; // Maybe have to calculate this from padding and font size?
    static final int barHeight = 29;
    private Stack<VBox> stack = new Stack<>();


    private List<BorderPane> rList = new LinkedList<>();
    private List<VBox> rowList = new LinkedList<VBox>();
    private List<VBox> lineNoList = new ArrayList<VBox>();
    private int counter = 1;
    private int carry = 0;


    private ScrollPane scrollPane;
    private VBox lineNo;
    private VBox rows;

    private TextField lastTf;
    private ChangeListener<? extends String> lastTfListener = (ov, oldValue, newValue) -> {
        newRow();
    };

    private AnchorPane createBar() {
        GridPane premisesAndConclusion = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(45);
        column2.setPercentWidth(10);
        column3.setPercentWidth(45);
        premisesAndConclusion.getColumnConstraints().addAll(column1,column2,column3);
        TextField premises = new TextField();
        premises.getStyleClass().add("myText");
        TextField turnstile = new TextField("⊢");
        turnstile.getStyleClass().add("myText");
        turnstile.setAlignment(Pos.CENTER);
        turnstile.setEditable(false);
        turnstile.setFocusTraversable(false);
        TextField conclusion = new TextField();
        conclusion.getStyleClass().add("myText");
        premisesAndConclusion.add(premises, 0, 0);
        premisesAndConclusion.add(turnstile, 1, 0);
        premisesAndConclusion.add(conclusion, 2, 0);

        AnchorPane bar = new AnchorPane(premisesAndConclusion);
        bar.setTopAnchor(premisesAndConclusion, 0.0);
        bar.setRightAnchor(premisesAndConclusion, 20.0);
        bar.setLeftAnchor(premisesAndConclusion, 20.0);
        bar.setBottomAnchor(premisesAndConclusion, 0.0);
        bar.setPrefHeight(barHeight);
        bar.setMinHeight(barHeight);
        bar.setMaxHeight(barHeight);
        return bar;
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
        AnchorPane ap = new AnchorPane(hb);
        ScrollPane sp = new ScrollPane();
        sp.getStyleClass().add("fit");
        ap.setTopAnchor(hb, 0.0);
        ap.setRightAnchor(hb, 0.0);
        ap.setLeftAnchor(hb, 0.0);
        ap.setBottomAnchor(hb, 0.0);
        scrollPane = sp;
        hb.heightProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.doubleValue() > oldValue.doubleValue()) { // Change this to only trigger on new row!!
                sp.setVvalue(1.0);                                 // Otherwise it will scroll down when you insert a row in the middle
            }
        });
        sp.setContent(ap);
        AnchorPane proofPane = new AnchorPane(sp);
        proofPane.setTopAnchor(sp, 0.0);
        proofPane.setRightAnchor(sp, 0.0);
        proofPane.setLeftAnchor(sp, 0.0);
        proofPane.setBottomAnchor(sp, 0.0);

        rowList.add(rows);
        lineNoList.add(lineNo);

        return proofPane;
    }

    public ProofView(TabPane tabPane) {
        SplitPane sp = new SplitPane(createBar(), createProofPane());
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


    public void closeBox() {
        if (!stack.isEmpty()) {
            VBox vb = stack.pop();
            vb.getStyleClass().clear();
            vb.getStyleClass().add("closedBox");
            carry += carryAddClose;

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

        rList.add(bp);


    }

    public void openBox() {
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        carry += carryAddOpen;
        checkAndAdd(vb);
        stack.push(vb);
        rowList.add(rows);
        lineNoList.add(lineNo);

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
        /*
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

        }*/
//        System.out.println(rList.get(rList.size()-1).getParent().getChildrenUnmodifiable().size());
        //todo make sure that the lines gets updated correctly

        if(rList.size()==0){

        }
        //delete the closing part of the box
        else if(rList.get(rList.size()-1).getParent().getStyleClass().toString().equals("closedBox")){
            System.out.println("cl");
            ArrayList<VBox>vl=new ArrayList<>();
            VBox node=(VBox)rList.get(rList.size()-1).getParent();
            //Remove all the closing part of boxes that encloses the last row
            while(node.getParent() instanceof VBox){
                node.getStyleClass().clear();
                node.getStyleClass().add("openBox");
                vl.add(node);
               // carry-=carryAddOpen;
                node = (VBox) node.getParent();
            }

            //the boxes gets deleted in wrong order so we have to push it in reverse
            for(int i=0;i<vl.size();i++){
                node=vl.get((vl.size()-1)-i);
                stack.push(node);

            }
        }
        //delete open part of the box
        else if(rList.get(rList.size()-1).getParent().getChildrenUnmodifiable().size()==1&&
                rList.get(rList.size()-1).getParent().getStyleClass().toString().equals("openBox")){
            System.out.println("op");
            ((VBox)rList.get(rList.size()-1).getParent().getParent()).getChildren().remove(((VBox) rList.get(rList.size()-1).getParent().getParent()).getChildren().size()-1);
            rList.remove(rList.size()-1);
            lineNo.getChildren().remove(lineNo.getChildren().size() - 1);
            counter--;
         //   carry-=carryAddClose;
            if (!stack.isEmpty())
                stack.pop();

        }
        //delete row
        else if(rList.get(rList.size()-1).getParent().getChildrenUnmodifiable().size()>0){
            System.out.println("remove");
            ((VBox)rList.get(rList.size()-1).getParent()).getChildren().remove(((VBox) rList.get(rList.size()-1).getParent()).getChildren().size()-1);
            rList.remove(rList.size()-1);
            counter--;
            lineNo.getChildren().remove(lineNo.getChildren().size() - 1);

        }


    }
    public void rowInserted(){}

}