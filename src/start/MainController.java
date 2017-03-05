package start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import view.ProofTab;
import view.ProofView;
import view.View;
import view.WelcomeView;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import model.Proof;
import java.util.prefs.Preferences;

public class MainController implements Initializable {
	
	//for testing, should be deleted
	private int counter = 0;
	HashMap<Tab,View> activeViews = new HashMap<Tab, View>();
	
    @FXML
    private TabPane tabPane;

    private ProofView currentProof;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void newProof(ActionEvent event) {
    	View view = new ProofView(tabPane, new Proof());
    	activeViews.put(view.getTab(), view);
    	//TODO: remove mapping when tab is closed
    }

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
    
    @FXML
    void saveProof(ActionEvent event) {
    	View view = getCurrentView();
    	if(view instanceof ProofView == false){
    		System.out.println("Not a proof, not saving");
    		return;
    	}
    	else{
    		ProofView pView = (ProofView)view;
    		if(pView.getPath() == null ){
    			//if no path is set for current proof, call other method
    			saveProofAs(null);
    			return;
    		}
    		try{
    			IOHandler.saveProof(pView.getProof(), pView.getPath());
    		}
    		catch(Exception e){
    			System.out.println("saveProof\n"+e);
    			//Inform user what went wrong
    		}
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
    		activeViews.put(openedProofView.getTab(), openedProofView);
    	}catch(Exception e){
    		System.out.println(e);
    		return;
    	}
    	//open new tab to display openedProof
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Preferences prefs = Preferences.userRoot().node("General"); // Inställningar i noden "General"
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab instanceof ProofTab) {
                ProofTab temp = (ProofTab) newTab;
                currentProof = temp.getView();
            }
        });
        if (prefs.getBoolean("showWelcome", true)) { // Om showWelcome-paret ej existerar, returnera true
            new WelcomeView(tabPane);
        }
    }
    
    //Get the view corresponding to the currently active tab
    View getCurrentView(){
    	return activeViews.get( tabPane.getSelectionModel().getSelectedItem());
    }
}

