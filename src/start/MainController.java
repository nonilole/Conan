package start;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button openBoxButton;

    @FXML
    private Button newRowButton;
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
        Preferences prefs = Preferences.userRoot().node("General");
        switch (caller.getText()) {
            case "Light theme":
                scene.getStylesheets().add("minimalistStyle.css");
                prefs.putInt("theme", 0);
                break;
            case "Dark theme":
                scene.getStylesheets().add("gruvjan.css");
                prefs.putInt("theme", 1);
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
        ProofView pv = new ProofView(tabPane, new Proof());
    }

    private ProofView convertProofView(View view) {
        if (view == null || !(view instanceof ProofView))
            return null;
        return (ProofView) view;
    }

    @FXML
    void closeTab(ActionEvent event) {
        if (currentTab != null)
            tabPane.getTabs().remove(currentTab);
    }

    @FXML
    void newRow(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        pv.newRow();
    }

    @FXML
    void insertBelowAfterMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowNumberLastFocusedTF();
        if (rowNumber != -1)
            pv.addRowAfterBox(rowNumber);
    }

    @FXML
    void openBox(ActionEvent event) { // Remove this later
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowNumberLastFocusedTF();
        if (rowNumber != -1)
            pv.insertNewBox(rowNumber);
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
        int rowNumber = pv.getRowNumberLastFocusedTF();
        if (rowNumber != -1) {
            pv.deleteRow(rowNumber);
        }
    }

    @FXML
    void insertAboveMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowNumberLastFocusedTF();
        if (rowNumber != -1)
            pv.insertNewRow(rowNumber, BoxReference.BEFORE);
    }

    @FXML
    void insertBelowMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowNumberLastFocusedTF();
        if (rowNumber != -1)
            pv.insertNewRow(rowNumber, BoxReference.AFTER);
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
        if (file == null) {
            System.out.println("Path not set, file not saved");
            return;
        }
        if (file.getAbsolutePath().endsWith(".proof") == false) {
            file = new File(file.getAbsolutePath() + ".proof");
        }

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
        ProofView pView = convertProofView(getCurrentView());
        if (pView == null)
            return;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("LaTeX", "*.tex"),
                new ExtensionFilter("All Files", "*.*"));
        File file = fc.showSaveDialog(tabPane.getScene().getWindow());
        if (file == null)
            return;
        if (file.getAbsolutePath().endsWith(".tex") == false) {
            file = new File(file.getAbsolutePath() + ".tex");
        }
        try {
            ExportLatex.export(pView.getProof(), file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openProof(ActionEvent event) {
        ProofView openedProofView;
        try {
            openedProofView = IOHandler.openProof(tabPane);
            if (openedProofView == null) {
                return;
            }
            openedProofView.displayLoadedProof();
        } catch (Exception e) {
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

    @FXML
    void showShortcuts(ActionEvent event) {
        new ShortcutsView(tabPane);
    }

    public void createTooltip() {
        //Toolbar
        saveButton.setTooltip(new Tooltip("Save Proof (CTRL+S)"));
        loadButton.setTooltip(new Tooltip("Open Proof (CTRL+O)"));
        newProofButton.setTooltip(new Tooltip("New Proof (CTRL+N)"));
        undoButton.setTooltip(new Tooltip("Undo (CTRL+Z)"));
        redoButton.setTooltip(new Tooltip("Redo (CTRL+Y/CTRL+SHIFT+Z)"));
        openBoxButton.setTooltip(new Tooltip("Open Box (CTRL+B)"));
        newRowButton.setTooltip(new Tooltip("Add row after box (Shift+Enter)"));
        verification.setTooltip(new Tooltip("Turn on/off verification"));
        generation.setTooltip(new Tooltip("Turn on/off automatic rule application"));

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
        forallIntroButton.setTooltip(new Tooltip("For All-Introduction"));
        forallElimButton.setTooltip(new Tooltip("For All-Elimination"));
        eqElimButton.setTooltip(new Tooltip("Equality-Elimination"));
        existsIntroButton.setTooltip(new Tooltip("There Exists-Introduction"));
        existsElimButton.setTooltip(new Tooltip("There Exists-Elimination"));
        copyButton.setTooltip(new Tooltip("Copy"));
        assButton.setTooltip(new Tooltip("Assumption"));
        freshButton.setTooltip(new Tooltip("Fresh Variable"));
        premiseButton.setTooltip(new Tooltip("Premise"));

        //Derived Rules
        mtButton.setTooltip(new Tooltip("Modus Tollens"));
        doubleNegButton.setTooltip(new Tooltip("Double Negation-Introduction"));
        pbcButton.setTooltip(new Tooltip("Proof by Contradiction"));
        lemButton.setTooltip(new Tooltip("Law of Excluded Middle"));

        //Symbols
        impButton.setTooltip(new Tooltip("Implication (type im to insert)"));
        andButton.setTooltip(new Tooltip("And (type an to insert)"));
        orButton.setTooltip(new Tooltip("Or (type or to insert)"));
        negButton.setTooltip(new Tooltip("Negation (type ne to insert)"));
        forallButton.setTooltip(new Tooltip("For All (type fa to insert)"));
        existsButton.setTooltip(new Tooltip("There Exists (type te to insert)"));
        contraButton.setTooltip(new Tooltip("Contradiction (type co to insert)"));
    }

    @FXML
    void showWelcome(ActionEvent event) {
        new WelcomeView(tabPane);
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
                if (newTab != null) {
                    new Thread(new Task<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Thread.sleep(100); // Only reliable way is to wait for all nodes to be created
                            return null;
                        }

                        @Override
                        public void succeeded() {
                            currentTab.getView().focusFirst();
                        }

                    }).start();
                }
            } else {
                currentTab = null;
            }
        });
        if (prefs.getBoolean("showWelcome", true)) { // Om showWelcome-paret ej existerar, returnera true
            showWelcome(null);
        } else {
            newProof(null);
        }
        createTooltip();
    }

    //Get the view corresponding to the currently active tab
    private View getCurrentView() {
        if (currentTab != null)
            return currentTab.getView();
        return null;
    }
}

