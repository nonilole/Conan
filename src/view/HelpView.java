package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class HelpView extends Symbolic implements View {
    ViewTab tab;

    public HelpView(TabPane tabPane) {
        //premises.setId("premises");
        //conclusion.setId("conclusion");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20.0);
        gridPane.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        String sTitle = "Learn more about Conan";
        String sWelcomeText = "Conan is a tool developed for providing assistance when constructing proofs in natural deduction for first-order logic.\n\n"
        		+ "If you want to learn more about how to construct a proof and how Conan works, please follow the link below.";
        String sParserText = "If you want to learn more about how expressions are parsed and how the editor interpreters what the user types into the expression field, please follow the link below.\n\n";
        String sTab = "Help";
        Label title = new Label(sTitle);
        title.getStyleClass().add("myTitle");
        Label welcomeText = new Label(sWelcomeText);
        Label parserText = new Label(sParserText);
        welcomeText.getStyleClass().add("infoText");
        welcomeText.setWrapText(true);
        parserText.getStyleClass().add("infoText");
        parserText.setWrapText(true);

        //link to a proof example
        Hyperlink help = new Hyperlink("Instructions on how to use Conan");
        help.getStyleClass().add("infoText");

        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new InstructionsView(tabPane);
            }
        });
        
        Hyperlink parser = new Hyperlink("Parser information");
        parser.getStyleClass().add("infoText");

        parser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new ParseInfoView(tabPane);
            }
        });
        
        gridPane.add(title, 0, 0);
        gridPane.add(welcomeText, 0, 1);
        gridPane.add(help, 0, 2);
        gridPane.add(parserText, 0, 3);
        gridPane.add(parser, 0, 4);
        //lägg till fler
        
        
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(help, HPos.CENTER);
        GridPane.setHalignment(parser, HPos.CENTER);

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


    /**
     * @param A string containing a comma separated list of formulas
     * @return an array with the input formulas split up
     * @throws IOException
     */
    public String[] splitFormulas(String str) throws IOException {
        StringReader strR = new StringReader(str);
        ArrayList<String> formulaList = new ArrayList<String>();

        int next;
        StringBuilder strB = new StringBuilder();
        int unmatchedLeftPar = 0;
        while ((next = strR.read()) != -1) {
            char ch = (char) next;

            switch (ch) {
                case ',':
                    if (unmatchedLeftPar == 0) {
                        formulaList.add(strB + "");
                        strB = new StringBuilder();
                        continue;
                    }
                    break;

                case '(':
                    unmatchedLeftPar++;
                    break;

                case ')':
                    unmatchedLeftPar = (unmatchedLeftPar == 0) ? 0 : --unmatchedLeftPar;
                    break;

                default:
                    break;
            }
            if(ch != ' '){
            	strB.append(ch);
            }

        }
        formulaList.add(strB + "");
        return formulaList.toArray(new String[formulaList.size()]);
    }


	@Override
	public void focusFirst() {
		// TODO Auto-generated method stub
		
	}
}


