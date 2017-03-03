package view;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CommonPanes {
    public static HBox premisesAndConclusion(String sPremises, String sConclusion) {
        HBox layout = new HBox();
        TextField premises = new TextField(sPremises);
        premises.getStyleClass().add("myText");
        TextField turnstile = new TextField("⊢");
        turnstile.getStyleClass().add("myText");
        turnstile.setPrefWidth(26.0);
        turnstile.setAlignment(Pos.CENTER);
        turnstile.setEditable(false);
        turnstile.setFocusTraversable(false);
        TextField conclusion = new TextField(sConclusion);
        conclusion.getStyleClass().add("myText");
        layout.getChildren().addAll(premises, turnstile, conclusion);
        layout.setHgrow(premises, Priority.ALWAYS);
        layout.setHgrow(turnstile, Priority.NEVER);
        layout.setHgrow(conclusion, Priority.ALWAYS);
        layout.setMinWidth(0.0);
        layout.setPrefWidth(500.0);
        layout.setMaxWidth(500.0);
        layout.setMinHeight(30.0);
        layout.setPrefHeight(30.0);
        layout.setMaxHeight(30.0);
        return layout;
    }
    public static HBox premisesAndConclusion() {
        return premisesAndConclusion("","");
    }
}
