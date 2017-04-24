package start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import view.*;


import view.ProofView;
import view.View;
import view.ViewTab;
import view.WelcomeView;
import view.InferenceRuleView;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import model.BoxReference;
import model.Proof;
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

    @FXML
    void verificationToggle(ActionEvent event) {

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
        if(view instanceof ProofView){
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
        if(rowNumber!=-1) {
            pv.getProof().deleteRow(rowNumber);
        }
    }

    @FXML
    void insertAboveMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowIndexLastFocusedTF();
        pv.getProof().insertNewRow(rowNumber,BoxReference.BEFORE);
    }

    @FXML
    void insertBelowMenu(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null)
            return;
        int rowNumber = pv.getRowIndexLastFocusedTF();
        pv.getProof().insertNewRow(rowNumber,BoxReference.AFTER);
    }

    @FXML
    void saveProof(ActionEvent event) {
        ProofView pv = convertProofView(getCurrentView());
        if (pv == null) {
            System.out.println("Not a proof, not saving");
            return;
        }
        if(pv.getPath() == null ){
            //if no path is set for current proof, call other method
            saveProofAs(null);
            return;
        }
        try{
            IOHandler.saveProof(pv.getProof(), pv.getPath());
        }
        catch(Exception e){
            System.out.println("saveProof\n"+e);
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
    	if(view instanceof ProofView == false){
    		System.out.println("Not a proof, not saving");
    		return;
    	}
    	else{
    		ProofView pView = (ProofView)view;
    		try{
    			IOHandler.saveProof(pView.getProof(), file.getPath());
    			pView.setName(file.getName());
    			pView.setPath(file.getPath());
    			pView.getTab().setText(pView.getName());
    		}
    		catch(Exception e){
    			System.out.println("saveProofAs\n"+e);
    			//e.printStackTrace();
    			//Inform user what went wrong
    		}
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences prefs = Preferences.userRoot().node("General"); // Inställningar i noden "General"
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab instanceof ViewTab) {
                currentTab = (ViewTab) newTab;
            }
        });
        if (prefs.getBoolean("showWelcome", true)) { // Om showWelcome-paret ej existerar, returnera true
            new WelcomeView(tabPane);
        }
    }

    //Get the view corresponding to the currently active tab
    private View getCurrentView() {
            return currentTab.getView();
    }
}

