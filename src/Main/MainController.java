package Main;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
        //create tab, add it to tabPane....Create proof object?
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

