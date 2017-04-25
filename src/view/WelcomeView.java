package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Proof;

import java.util.prefs.Preferences;

public class WelcomeView extends Symbolic implements View {
    ViewTab tab;
    TextField premises;
    TextField conclusion;
    CheckBox notAgain;

    public WelcomeView(TabPane tabPane) {
    	//premises.setId("premises");
    	//conclusion.setId("conclusion");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20.0);
        gridPane.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        String sTitle = "Welcome to Conan";
        String sWelcomeText = "Conan is tool developed for providing assistance when constructing proofs in natural deduction for first-order logic.\n\n"
        		+ "Feel free to enter premises and conclusions for something you want to prove and then click continue to proceed.\n\n"
        		+ "If you want to learn more about how to use the interface, please follow the link below.";
        String sTab = "Welcome";
        Label title = new Label(sTitle);
        title.getStyleClass().add("myTitle");
        Label welcomeText = new Label(sWelcomeText);
        welcomeText.getStyleClass().add("infoText");
        welcomeText.setWrapText(true);
        PremisesAndConclusion premisesAndConclusion = new PremisesAndConclusion();
        this.premises = premisesAndConclusion.getPremises();
        this.conclusion = premisesAndConclusion.getConclusion();
        this.premises.setId("expression");
        this.premises.setPromptText("Premise");
        premises.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				premises.setText(checkShortcut(newValue));
			}
		});
        this.conclusion.setId("expression");
        this.conclusion.setPromptText("Conclusion");
        this.premises.focusedProperty().addListener((observable, oldValue, newValue) -> {
            lastFocusedTf = this.premises;
            caretPosition = this.premises.getCaretPosition();
        });
        this.conclusion.focusedProperty().addListener((observable, oldValue, newValue) -> {
            lastFocusedTf = this.conclusion;
            caretPosition = this.conclusion.getCaretPosition();
        });
        conclusion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				conclusion.setText(checkShortcut(newValue));
			}
		});

        Hyperlink help = new Hyperlink("Tell me more about the interface");
        help.getStyleClass().add("infoText");

        help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new InstructionsView(tabPane);
			}
		});
        

        this.notAgain = new CheckBox("Do not show again");
        Button butNext = new Button("Continue");
        butNext.setOnAction(event -> {
            Preferences prefs = Preferences.userRoot().node("General");
            TabPane tabPane1 = tab.getTabPane();
            if (!this.notAgain.isIndeterminate() && this.notAgain.selectedProperty().getValue()) {
                prefs.putBoolean("showWelcome", false); // Om knappen är checked, visa inte välkomsttabben.
            }
            tabPane1.getTabs().remove(tab);
            new ProofView(tabPane1, new Proof(), premises.getText(), conclusion.getText());
        });
        gridPane.add(title, 0, 0);
        gridPane.add(welcomeText, 0, 1);
        gridPane.add(help, 0, 2);
        gridPane.add(premisesAndConclusion, 0, 3);
        gridPane.add(this.notAgain, 0, 4);
        gridPane.add(butNext, 0, 4);
        
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(help, HPos.CENTER);

        GridPane.setHalignment(premisesAndConclusion, HPos.CENTER);
        GridPane.setValignment(premisesAndConclusion, VPos.CENTER);
        GridPane.setHalignment(notAgain, HPos.LEFT);
        GridPane.setHalignment(butNext, HPos.RIGHT);
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AnchorPane ap = new AnchorPane(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        this.tab = new ViewTab(sTab, this);
        this.tab.setContent(ap);
        tabPane.getTabs().add(this.tab);
        tabPane.getSelectionModel().select(this.tab);
    }

    @Override
    public ViewTab getTab() {
        return this.tab;
    }
    
    public String checkShortcut(String newValue){
		newValue = newValue.replaceAll("!|ne|no", "¬");
		newValue = newValue.replaceAll("&|an", "∧");
		newValue = newValue.replaceAll("->", "→");
		newValue = newValue.replaceAll("im", "→");
		newValue = newValue.replaceAll("fa", "∀");
		newValue = newValue.replaceAll("(?<!f)or", "∨");
		newValue = newValue.replaceAll("ex", "∃");
		newValue = newValue.replaceAll("te", "∃");
		return newValue;
	}
}

