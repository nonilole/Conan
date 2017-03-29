package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import model.Proof;

import java.util.prefs.Preferences;

public class WelcomeView implements View {
    ViewTab tab;
    TextField premises;
    TextField conclusion;
    CheckBox notAgain;

    public WelcomeView(TabPane tabPane) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20.0);
        gridPane.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        String sTitle = "Conan";
        String sWelcomeText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ante est, placerat quis ultricies sed, varius nec ante. Nullam interdum eu tortor quis faucibus. Duis accumsan purus ac diam porta, eget semper arcu pulvinar. Sed et ornare ligula. Morbi sollicitudin pretium eros, vitae feugiat ante tincidunt vel. Cras aliquam lobortis neque, non rutrum orci volutpat et. Nam at tincidunt mauris. Phasellus lobortis lorem dolor, sit amet tempus nulla pellentesque nec. Aenean a venenatis lectus.";
        sWelcomeText += " " + sWelcomeText;
        String sTab = "Welcome";
        Label title = new Label(sTitle);
        title.getStyleClass().add("myTitle");
        Label welcomeText = new Label(sWelcomeText);
        welcomeText.getStyleClass().add("myText");
        welcomeText.setWrapText(true);
        RowConstraints rowC1 = new RowConstraints();
        RowConstraints rowC2 = new RowConstraints();
        RowConstraints rowC3 = new RowConstraints();
        RowConstraints rowC4 = new RowConstraints();
        RowConstraints rowC5 = new RowConstraints();
        rowC1.setPrefHeight(20.0);
        rowC2.setPrefHeight(20.0);
        rowC3.setFillHeight(true);
        rowC4.setPrefHeight(20.0);
        rowC5.setPrefHeight(20.0);
        HBox premisesAndConclusion = CommonPanes.premisesAndConclusion();
        this.premises = (TextField) premisesAndConclusion.getChildren().get(0);
        this.conclusion = (TextField) premisesAndConclusion.getChildren().get(2);

        Hyperlink help = new Hyperlink("Help me!");
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
        //gridPane.getRowConstraints().addAll(rowC1, rowC2, rowC3, rowC4, rowC5);
        gridPane.add(title, 0, 0);
        gridPane.add(help, 0, 1);
        gridPane.add(welcomeText, 0, 2);
        gridPane.add(premisesAndConclusion, 0, 3);
        gridPane.add(this.notAgain, 0, 4);
        gridPane.add(butNext, 0, 4);
        gridPane.setHalignment(title, HPos.CENTER);
        gridPane.setValignment(title, VPos.CENTER);
        gridPane.setHalignment(help, HPos.CENTER);
        gridPane.setHalignment(premisesAndConclusion, HPos.CENTER);
        gridPane.setValignment(premisesAndConclusion, VPos.CENTER);
        gridPane.setHalignment(notAgain, HPos.LEFT);
        gridPane.setHalignment(butNext, HPos.RIGHT);
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AnchorPane ap = new AnchorPane(scrollPane);
        ap.setTopAnchor(scrollPane, 0.0);
        ap.setRightAnchor(scrollPane, 0.0);
        ap.setBottomAnchor(scrollPane, 0.0);
        ap.setLeftAnchor(scrollPane, 0.0);
        this.tab = new ViewTab(sTab, this);
        this.tab.setContent(ap);
        tabPane.getTabs().add(this.tab);
        tabPane.getSelectionModel().select(this.tab);
    }

    @Override
    public ViewTab getTab() {
        return this.tab;
    }
}

