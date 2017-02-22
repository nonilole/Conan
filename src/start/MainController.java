package start;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import view.ProofView;

import java.util.Stack;

import javafx.event.ActionEvent;

public class MainController {
	
	
	
    @FXML
    private TabPane tabPane;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void newProof(ActionEvent event) {
    	ProofView.newProof(tabPane);
        
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

