package Main;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Stack;

import javafx.event.ActionEvent;

public class MainController {

	Stack<Tab> tabs = new Stack<Tab>();
	int counter = 0;
	
    @FXML
    private TabPane tabPane;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void newProof(ActionEvent event) {
    	Tab tab = new Tab(""+counter++);
    	
    	TableColumn tableColumnLeft = new TableColumn();
    	tableColumnLeft.setText("Expression");
    	tableColumnLeft.setMaxWidth(5000);
    	tableColumnLeft.setMinWidth(10);
    	tableColumnLeft.setEditable(true);
    	tableColumnLeft.setPrefWidth(356);
    	
    	
    	TableColumn tableColumnRight = new TableColumn();
    	tableColumnRight.setText("Type");
    	tableColumnRight.setMaxWidth(5000);
    	tableColumnRight.setMinWidth(10);
    	tableColumnRight.setEditable(true);
    	tableColumnRight.setPrefWidth(356);
    	
    	
    	TableView tableView = new TableView();
    	tableView.setPadding(new Insets(0, 0, 0, 0));
    	tableView.getColumns().addAll(tableColumnLeft, tableColumnRight);
    	
    	AnchorPane anchorPane = new AnchorPane(tableView);
    	anchorPane.setPrefWidth(714);
    	anchorPane.setPrefHeight(460);
    	anchorPane.setPadding(new Insets(0, 0, 0, 0));
    	anchorPane.setScaleShape(true);
    	anchorPane.setCacheShape(true);
    	anchorPane.setCenterShape(true);
    	
    	tab.setContent(anchorPane);
    	tabs.push(tab);
    	tabPane.setPadding(new Insets(0, 0, 0, 0));
        tabPane.getTabs().add(tabs.peek());
        
    }

    @FXML
    void openInstructions(ActionEvent event) {

    }

    @FXML
    void ruleButtonPressed(ActionEvent event) {
        System.out.println("Yay!");
    }

    @FXML
    void symbolButtonPressed(ActionEvent event) {

    }

}

