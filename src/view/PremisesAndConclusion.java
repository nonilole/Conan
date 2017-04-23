package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Parser;
import model.ParseException;

// Kanske vill skriva om ProofListener och låta denna lyssna på ett bevis istället.
public class PremisesAndConclusion extends HBox {
    Parser parser = new Parser();
    private TextField premises;
    private Label turnstile;
    private TextField conclusion;
    private void applyStyleIf(TextField expression, boolean bool, String style) {
        expression.getStyleClass().removeIf((s) -> s.equals(style));
        if (bool) {
            expression.getStyleClass().add(style);
        }
    }
    private void parseAndStyle(TextField tf, String s) {
        try {
            if (!s.equals(""))
                parser.parse(s);
            applyStyleIf(tf,false,"bad");
        }
        catch (ParseException e) {
            applyStyleIf(tf,true,"bad");
        }
    }

    private void setPrefs() {
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
    public TextField getPremises() {
        return this.premises;
    }

    public TextField getConclusion() {
        return this.conclusion;
    }

    public PremisesAndConclusion(String sPremises, String sConclusion) {
        premises = new TextField(sPremises);
        premises.textProperty().addListener((ov, oldValue, newValue) -> {
            parseAndStyle(premises, newValue);
        });
        parseAndStyle(premises, sPremises);
        premises.getStyleClass().add("myText");
        turnstile = new Label("⊢");
        turnstile.getStyleClass().add("myText");
        turnstile.setPrefWidth(26.0);
        turnstile.setPadding(new Insets(5,0,0,0));
        turnstile.setAlignment(Pos.CENTER);
        conclusion = new TextField(sConclusion);
        conclusion.textProperty().addListener((ov, oldValue, newValue) -> {
            parseAndStyle(conclusion, newValue);
        });
        parseAndStyle(conclusion, sConclusion);
        conclusion.getStyleClass().add("myText");
        this.getChildren().addAll(premises, turnstile, conclusion);
        setPrefs();
    }
    public PremisesAndConclusion() {
        this("","");
    }
}
