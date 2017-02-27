package view;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.ProofListener;
import model.ProofTab;


public class ProofView implements ProofListener{
    static final int carryAddOpen = 3; // Magic
    static final int carryAddClose = 5; // Maybe have to calculate this from padding and font size?
    static final int barHeight = 29;
    private Stack<VBox> stack = new Stack<>();
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
        GridPane threeCells = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(45);
        column2.setPercentWidth(10);
        column3.setPercentWidth(45);
        threeCells.getColumnConstraints().addAll(column1,column2,column3);
        TextField tf1 = new TextField();
        tf1.getStyleClass().add("myText");
        TextField tf2 = new TextField("⊢");
        tf2.getStyleClass().add("myText");
        tf2.setAlignment(Pos.CENTER);
        tf2.setEditable(false);
        tf2.setFocusTraversable(false);
        TextField tf3 = new TextField();
        tf3.getStyleClass().add("myText");
        threeCells.add(tf1, 0, 0);
        threeCells.add(tf2, 1, 0);
        threeCells.add(tf3, 2, 0);

        AnchorPane bar = new AnchorPane(threeCells);
        bar.setTopAnchor(threeCells, 0.0);
        bar.setRightAnchor(threeCells, 20.0);
        bar.setLeftAnchor(threeCells, 20.0);
        bar.setBottomAnchor(threeCells, 0.0);
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
    }

    public void openBox() {
        VBox vb = new VBox();
        vb.getStyleClass().add("openBox");
        carry += carryAddOpen;
        checkAndAdd(vb);
        stack.push(vb);
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
    public void rowInserted(){}

}
