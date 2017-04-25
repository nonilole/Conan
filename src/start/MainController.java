package start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.BoxReference;
import model.Proof;
import model.rules.Premise;
import view.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainController implements Initializable {
    @FXML
    private CheckBox verification;
    @FXML
    private CheckBox generation;
    @FXML
    private TabPane tabPane;

    private ViewTab currentTab;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    //Toolbar buttons
    @FXML
    private Button loadButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button newProofButton;

    //Inference Rules Buttons
    @FXML
    private Button andIntroButton;

    @FXML
    private Button andElim1Button;

    @FXML
    private Button andElim2Button;

    @FXML
    private Button orIntro1Button;

    @FXML
    private Button orIntro2Button;

    @FXML
    private Button orElimButton;

    @FXML
    private Button impIntroButton;

    @FXML
    private Button impElimButton;

    @FXML
    private Button contraElimButton;

    @FXML
    private Button negIntroButton;

    @FXML
    private Button negElimButton;

    @FXML
    private Button doubleNegElimButton;
    @FXML
    private Button eqIntroButton;
    @FXML
    private Button forallIntroButton;
    @FXML
    private Button forallElimButton;
    @FXML
    private Button eqElimButton;
    @FXML
    private Button existsIntroButton;
    @FXML
    private Button existsElimButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button assButton;
    @FXML
    private Button freshButton;
    @FXML
    private Button premiseButton;









    //Derived Rules Buttons
    @FXML
    private Button mtButton;

    @FXML
    private Button doubleNegButton;

    @FXML
    private Button pbcButton;

    @FXML
    private Button lemButton;

    //Symbol Buttons
    @FXML
    private Button impButton;

    @FXML
    private Button andButton;

    @FXML
    private Button orButton;

    @FXML
    private Button negButton;

    @FXML
    private Button forallButton;

    @FXML
    private Button existsButton;

    @FXML
    private Button contraButton;




    @FXML
    void verificationToggle(ActionEvent event) {

    }
    @FXML
    void setTheme(ActionEvent event) {
        Scene scene = tabPane.getScene();
        scene.getStylesheets().clear();
        MenuItem caller = (MenuItem) event.getSource();
        switch(caller.getText()) {
            case "Dark theme":
                scene.getStylesheets().add("gruvjan.css");
                break;
            case "Light theme":
                scene.getStylesheets().add("minimalistStyle.css");
                break;
        }
    }

    @FXML
    void generationToggle(ActionEvent event) {
        Preferences prefs = Preferences.userRoot().node("General");
        if (generation.isIndeterminate())
            return;
        if (this.generation.selectedProperty().getValue()) {
            prefs.putBoolean("generate", true);
            verification.setSelected(true);
            verificationToggle(event);
        } else {
            prefs.putBoolean("generate", false);
        }
    }

    @FXML
    void newProof(ActionEvent event) {
        new ProofView(tabPane, new Proof());
    }

    private ProofView convertProofView(View view) {
        if (view instanceof ProofView) {
            return (ProofView) view;
        } else {
            return null;
        }
    }

    @FXML
    void newRow(ActionEvent event) {
        //System.out.println("mainc newRow");
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.newRow();
    }

    @FXML
    void openBox(ActionEvent event) { // Remove this later
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.openBox();
    }

    @FXML
    void closeBox(ActionEvent event) { // Remove this later
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.closeBox();
    }

    @FXML
    void undo(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.undo();
    }

    @FXML
    void redo(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.redo();
    }

    @FXML
    void newProofButton(ActionEvent event) { // Remove this later
        new WelcomeView(tabPane);
    }

    @FXML
    void showInferenceRules(ActionEvent event) {
        new InferenceRuleView(tabPane);
    }

    @FXML
    void symbolButtonPressed(ActionEvent event) {
        Object obj = getCurrentView();
        if (obj instanceof Symbolic) {
            Symbolic sym = (Symbolic) obj;
            sym.addSymbol(event);
        }
    }

    @FXML
    void ruleButtonPressed(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv != null) {
            pv.addRule(event);
        }
    }

    @FXML
    void deleteRowMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowIndexLastFocusedTF();
        if (rowNumber != -1) {
            pv.getProof().deleteRow(rowNumber);
        }
    }

    @FXML
    void insertAboveMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowIndexLastFocusedTF();
        pv.getProof().insertNewRow(rowNumber, BoxReference.BEFORE,0);
    }

    @FXML
    void insertBelowMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowIndexLastFocusedTF();
        pv.getProof().insertNewRow(rowNumber, BoxReference.AFTER, 0);
    }

    @FXML
    void saveProof(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null) {
            System.out.println("Not a proof, not saving");
            return;
        }
        if (pv.getPath() == null) {
            //if no path is set for current proof, call other method
            saveProofAs(null);
            return;
        }
        try {
            IOHandler.saveProof(pv.getProof(), pv.getPath());
        } catch (Exception e) {
            System.out.println("saveProof\n" + e);
            //Inform user what went wrong
        }
    }

    @FXML
    void saveProofAs(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Proofs", "*.proof"),
                new ExtensionFilter("All Files", "*.*"));
        File file = fc.showSaveDialog(tabPane.getScene().getWindow());

        View view = getCurrentView();
        if (view instanceof ProofView == false) {
            System.out.println("Not a proof, not saving");
            return;
        } else {
            ProofView pView = (ProofView) view;
            try {
                IOHandler.saveProof(pView.getProof(), file.getPath());
                pView.setName(file.getName());
                pView.setPath(file.getPath());
                pView.getTab().setText(pView.getName());
            } catch (Exception e) {
                System.out.println("saveProofAs\n" + e);
                //e.printStackTrace();
                //Inform user what went wrong
            }
        }

    }

    @FXML
    void exportProofToLatex(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("LaTeX", "*.tex"),
                new ExtensionFilter("All Files", "*.*"));
        File file = fc.showSaveDialog(tabPane.getScene().getWindow());
        ProofView pView = convertProofView(getCurrentView());
        try {
            ExportLatex.export(pView.getProof(), file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openProof(ActionEvent event){
    	ProofView openedProofView;
    	try{
    		openedProofView = IOHandler.openProof(tabPane);
    		openedProofView.displayLoadedProof();
    	}catch(Exception e){
    		System.out.println("MainController.openProof exception:");
    		System.out.println(e);
    		e.printStackTrace();
    		return;
    	}
    }

    @FXML
    void showUserInstructions(ActionEvent event) {
        new InstructionsView(tabPane);
    }

    public void createTooltip(){
        //Toolbar
        saveButton.setTooltip(new Tooltip("Save Proof (CTRL+S)"));
        loadButton.setTooltip(new Tooltip("Open Proof (CTRL+O)"));
        newProofButton.setTooltip(new Tooltip("New Proof (CTRL+N)"));

        //Inference Rules
        andIntroButton.setTooltip(new Tooltip("And-Introduction"));
        andElim1Button.setTooltip(new Tooltip("And-Elimination 1"));
        andElim2Button.setTooltip(new Tooltip("And-Elimination 2"));
        orIntro1Button.setTooltip(new Tooltip("Or-Introduction 1"));
        orIntro2Button.setTooltip(new Tooltip("Or-Introduction 2"));
        orElimButton.setTooltip(new Tooltip("Or-Elimination"));
        impIntroButton.setTooltip(new Tooltip("Implication-Introduction"));
        impElimButton.setTooltip(new Tooltip("Implication-Elimination"));
        contraElimButton.setTooltip(new Tooltip("Contradiction-Elimination"));
        negIntroButton.setTooltip(new Tooltip("Negation-Introduction"));
        negElimButton.setTooltip(new Tooltip("Negation-Elimination"));
        doubleNegElimButton.setTooltip(new Tooltip("Double Negation-Elimination"));
        eqIntroButton.setTooltip(new Tooltip("Equality-Introduction"));
        eqElimButton.setTooltip(new Tooltip("Equality-Elimination"));
        existsIntroButton.setTooltip(new Tooltip("There Exists-Introduction"));
        existsElimButton.setTooltip(new Tooltip("There Exists-Elimination"));
        copyButton.setTooltip(new Tooltip("Copy"));
        assButton.setTooltip(new Tooltip("Assumption"));
        freshButton.setTooltip(new Tooltip("Fresh Variable"));
        premiseButton.setTooltip(new Tooltip("Premise"));

        //Derived Rules
        mtButton.setTooltip(new Tooltip("Modus Tollens"));
        doubleNegButton.setTooltip(new Tooltip("Double Negation"));
        pbcButton.setTooltip(new Tooltip("Proof by Contradiction"));
        lemButton.setTooltip(new Tooltip("Law of Exluded Middle"));

        //Symbols
        impButton.setTooltip(new Tooltip("Implication (type im to insert)"));
        andButton.setTooltip(new Tooltip("And (type an to insert)"));
        orButton.setTooltip(new Tooltip("Or (type or to insert)"));
        negButton.setTooltip(new Tooltip("Negation (type ne to insert)"));
        forallButton.setTooltip(new Tooltip("For All (type fa to insert)"));
        existsButton.setTooltip(new Tooltip("There Exists (type te to insert)"));
        contraButton.setTooltip(new Tooltip("Contradiction (type co to insert)"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences prefs = Preferences.userRoot().node("General"); // Inställningar i noden "General"
        if (prefs.getBoolean("generate", true)) {
            generation.setSelected(true);
            generationToggle(new ActionEvent());
        }
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab instanceof ViewTab) {
                currentTab = (ViewTab) newTab;
            }
        });
        if (prefs.getBoolean("showWelcome", true)) { // Om showWelcome-paret ej existerar, returnera true
            new WelcomeView(tabPane);
        }
        createTooltip();
    }

    //Get the view corresponding to the currently active tab
    private View getCurrentView() {
        return currentTab.getView();
    }
}

