package view;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Parser;
import model.ParseException;


public class premisesAndConclusion extends HBox {
    Parser parser = new Parser();
    private TextField premises;
    private TextField conclusion;
    private void applyStyleIf(TextField expression, boolean bool, String style) {
        expression.getStyleClass().removeIf((s) -> s.equals(style));
        if (bool) {
            expression.getStyleClass().add(style);
        }
    }
    private void parseAndStyle(TextField tf, String s) {
        try {
            parser.parse(s);
            applyStyleIf(tf,false,"bad");
        }
        catch (ParseException e) {
            applyStyleIf(tf,true,"bad");
        }
    }

    public premisesAndConclusion(String sPremises, String sConclusion) {
        premises = new TextField(sPremises);
        premises.textProperty().addListener((ov, oldValue, newValue) -> {
            parseAndStyle(premises, newValue);
        });
        parseAndStyle(premises, sPremises);
        premises.getStyleClass().add("myText");
        TextField turnstile = new TextField("⊢");
        turnstile.getStyleClass().add("myText");
        turnstile.setPrefWidth(26.0);
        turnstile.setAlignment(Pos.CENTER);
        turnstile.setEditable(false);
        turnstile.setFocusTraversable(false);
        conclusion = new TextField(sConclusion);
        conclusion.textProperty().addListener((ov, oldValue, newValue) -> {
            parseAndStyle(conclusion, newValue);
        });
        parseAndStyle(conclusion, sConclusion);
        conclusion.getStyleClass().add("myText");
        this.getChildren().addAll(premises, turnstile, conclusion);
        this.setHgrow(premises, Priority.ALWAYS);
        this.setHgrow(turnstile, Priority.NEVER);
        this.setHgrow(conclusion, Priority.ALWAYS);
        this.setMinWidth(0.0);
        this.setPrefWidth(500.0);
        this.setMaxWidth(500.0);
        this.setMinHeight(30.0);
        this.setPrefHeight(30.0);
        this.setMaxHeight(30.0);
    }
    public premisesAndConclusion() {
        this("","");
    }
}
