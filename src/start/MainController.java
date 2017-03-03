package start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import view.ProofTab;
import view.ProofView;
import view.WelcomeView;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TabPane tabPane;

    private ProofView currentProof;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void newProof(ActionEvent event) {new ProofView(tabPane);}

    @FXML
    void newRow(ActionEvent event) {
        if (currentProof != null) {
            currentProof.newRow();
        }
    }

    @FXML
    void openBox(ActionEvent event) { // Remove this later
        if (currentProof != null) {
            currentProof.openBox();
        }
    }

    @FXML
    void closeBox(ActionEvent event) { // Remove this later
        if (currentProof != null) {
            currentProof.closeBox();
        }
    }

    @FXML
    void deleteLastRow(ActionEvent event) { // Remove this later
        if (currentProof != null) {
            currentProof.rowDeleteLastRow();
        }
    }

    @FXML
    void newProofButton(ActionEvent event) { // Remove this later
        new WelcomeView(tabPane);
    }


    @FXML
    void openInstructions(ActionEvent event) { }

    @FXML
    void ruleButtonPressed(ActionEvent event) {
    }

    @FXML
    void symbolButtonPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab instanceof ProofTab) {
                ProofTab temp = (ProofTab) newTab;
                currentProof = temp.getView();
            }
        }
        );
        new WelcomeView(tabPane);
    }
}

