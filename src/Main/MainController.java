package Main;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    	tabs.push(new Tab(""+counter++));
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

