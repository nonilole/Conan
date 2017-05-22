package view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Parser;
import model.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static view.ViewUtil.applyStyleIf;
import static view.ViewUtil.consumeKeys;

// Kanske vill skriva om ProofListener och låta denna lyssna på ett bevis istället.
public class PremisesAndConclusion extends HBox {
    Parser parser = new Parser();
    private TextField premises;
    private Label turnstile;
    private TextField conclusion;
    Pattern regex = Pattern.compile("\\(.*?\\)|(,)");
    public void parseAndStyle(TextField tf, String s) {
        Matcher matcher = regex.matcher(s);
        StringBuffer newString = new StringBuffer();
        while (matcher.find()) { // Two sets of matches, replace the second set of matches with !, because they can't be written
            if (matcher.group(1) != null) {
                matcher.appendReplacement(newString, "!");
            } else {
                matcher.appendReplacement(newString, matcher.group(0));
            }
        }
        matcher.appendTail(newString);
        // Split at the !, which are not inputtable
        String[] formulas = newString.toString().split("!");

        for (String split : formulas) {
            try {
                if (!split.isEmpty())
                    parser.parse(split);

            } catch (ParseException e) {
                applyStyleIf(tf, true, "bad");
                return;
            }
        }
        applyStyleIf(tf, false, "bad");
    }

    private void setPrefs() {
        this.setHgrow(premises, Priority.ALWAYS);
        this.setHgrow(turnstile, Priority.NEVER);
        this.setHgrow(conclusion, Priority.ALWAYS);
        this.setMinWidth(0.0);
        this.setPrefWidth(800.0);
        this.setMaxWidth(800.0);
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
        turnstile.setPrefHeight(25);
        turnstile.setPrefWidth(26.0);
        turnstile.setPadding(new Insets(5,0,0,0));
        turnstile.setAlignment(Pos.CENTER);
        conclusion = new TextField(sConclusion);
        conclusion.textProperty().addListener((ov, oldValue, newValue) -> {
            parseAndStyle(conclusion, newValue);
        });
        parseAndStyle(conclusion, sConclusion);
        conclusion.getStyleClass().add("myText");
        consumeKeys(premises);
        consumeKeys(conclusion);
        this.getChildren().addAll(premises, turnstile, conclusion);
        setPrefs();
    }
    public PremisesAndConclusion() {
        this("","");
    }
    
}
