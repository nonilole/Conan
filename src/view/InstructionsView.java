package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import start.Constants;

public class InstructionsView extends ViewTab {
    
    private TabPane tabPane;
	
	private final static String tabName = "Instructions";
    
    public InstructionsView(TabPane tabPane) {
    	super(tabName, null);
    	this.tabPane = tabPane;
    	this.setContent(constructContent());     
        addAsTab();
        open();
    }
    
    /**
     * Constructs the contents of the view.
     * @return a Node that is aggregated by all the contents of the view.
     */
    private Node constructContent() {
    	/*
    	 * The view is composed as follows:
    	 * an anchor pane
    	 * 	└── a scroll pane
    	 * 		 └── a grid pane
    	 * 				├── A heading
    	 * 				├── User instructions
    	 *	 			└── A close button
    	 */
    	return constructAnchorPane();
    }
    
    /**
     * Constructs the complete anchor pane of this view.
     */
    private AnchorPane constructAnchorPane() {
		Node anchorChild = constructScrollPane(); 
		AnchorPane anchorPane = new AnchorPane(anchorChild);
		setAnchorBounds(anchorChild);
		return anchorPane;
    }
    
    /**
     * Sets the bounds for the contents of the anchor pane.
     * @param contents
     */
    private void setAnchorBounds(Node contents) {
    	AnchorPane.setTopAnchor(contents, 0.0);
		AnchorPane.setRightAnchor(contents, 0.0);
		AnchorPane.setBottomAnchor(contents, 0.0);
		AnchorPane.setLeftAnchor(contents, 0.0);
	}
    
    /**
     * Enables scrolling in the view.
     */
    private ScrollPane constructScrollPane() {
    	GridPane gridPane = constructGridPane();
    	ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }
    
    /**
     * Sets the layout of the component
     */
    private GridPane constructGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20.0);
        gridPane.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        
       
        
        Label heading = constructHeading();
        Node instructions = constructInstructions();
        Button closeButton = constructCloseButton();
        
        addChildren(gridPane, heading, instructions, closeButton);
        setAlignments(heading, instructions, closeButton);
        
        return gridPane;
	}
    
    private void addChildren(GridPane gridPane, Node heading, Node instructions, Node closeButton) {
    	gridPane.add(heading, 0, 0);
		gridPane.add(instructions, 0, 1);
		gridPane.add(closeButton, 0, 2);
    }
    
    /**
     * Sets the alignments for the children of the grid pane
     * @param heading The heading child.
     * @param instructions The instructions child.
     * @param closeButton The closeButton child.
     */
    private void setAlignments(Node heading, Node instructions, Node closeButton) {
    	GridPane.setHalignment(heading, HPos.CENTER);
    	GridPane.setValignment(heading, VPos.CENTER);
    	GridPane.setHalignment(closeButton, HPos.RIGHT);
	}

    
    /**
     * Constructs the view heading.
     */
    private Label constructHeading() {
    	final String text = "How to use Conan";
    	Label heading = new Label(text);
    	heading.getStyleClass().add("myTitle");
    	return heading;
    }
    
    private Node constructInstructions() {
    	GridPane instructionGrid = new GridPane();
    	instructionGrid.setVgap(20.0);
    	instructionGrid.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        
    	int rowNr = 0;
    	
        instructionGrid.add(configureLabel(introtxt,false), 0, ++rowNr);
        instructionGrid.add(configureLabel("Input:",true), 0, ++rowNr);
        instructionGrid.add(configureLabel(inputtxt,false), 0, ++rowNr);
        instructionGrid.add(getParserlink(),0,++rowNr);
        instructionGrid.add(configureLabel("Conclusion verification:",true), 0, ++rowNr);
        instructionGrid.add(configureLabel(conclusiontxt,false), 0, ++rowNr);
        instructionGrid.add(configureImage("ConclusionEntry.png"), 0, ++rowNr);
        instructionGrid.add(configureLabel("Verification and formula generation:",true), 0,++rowNr);
        instructionGrid.add(configureLabel(toggletxt,false), 0, ++rowNr);
        instructionGrid.add(configureImage("Toggles.png"), 0, ++rowNr);
        instructionGrid.add(configureLabel("Adding rows and navigation:",true), 0,++rowNr);
        instructionGrid.add(configureLabel(addingrowstxt,false), 0, ++rowNr);
        instructionGrid.add(getShortcutlink(),0,++rowNr);
        
        instructionGrid.add(configureLabel("Proof example:",true), 0, ++rowNr);
        instructionGrid.add(configureLabel("Presented below are example instructions for proving A∧B ⊢ B∧A (B and A from A and B). Feel free to create a new proof and imitate every step.",false), 0, ++rowNr);
        instructionGrid.add(configureLabel("First, locate the toolbox at the left part of the interface. This tool box provides easy access to frequently used proof components, such as rules of inference and unicode symbols used in logic.",false), 0, ++rowNr);
        instructionGrid.add(configureImage("exampleToolBox.png"), 0, ++rowNr);
        instructionGrid.add(configureLabel("Next, enter the conclusion B∧A in the proof header and the premise A∧B as the first row.",false), 0, ++rowNr);
        instructionGrid.add(configureImage("exampleSequent.png"), 0, ++rowNr);
        instructionGrid.add(configureLabel("The next step is to actually construct the proof. This is done by entering one deduction step per row in the proof sheet. Note in particular that each row consists of two or more columns.\n\n"
        		+ "In the first column, the formula of the deduction step is entered. In the second, the inference rule is entered. If the rule is dependent on previous rows, these are entered in the third and fourth columns.\n\n"
        		+ "Pay attention to the colours that indicate that you have reached your conclusion, or if you have made an error.", false), 0, ++rowNr);
        instructionGrid.add(configureImage("exampleProof.png"), 0, ++rowNr);
        instructionGrid.add(configureLabel("Congratulations! You have now completed the proof.", false), 0, ++rowNr);
        
        
        return instructionGrid;
	}
    
    private Hyperlink getShortcutlink(){
    	Hyperlink help = new Hyperlink("Show me shortcuts");
        help.getStyleClass().add("infoText");

        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new ShortcutsView(tabPane);
            }
        });
    	return help;
    }
    
    private Hyperlink getParserlink(){
    	Hyperlink parser = new Hyperlink("Show me logic input format");
        parser.getStyleClass().add("infoText");

        parser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new ParseInfoView(tabPane);
            }
        });
    	return parser;
    }
    
    private Label configureLabel(String contents, boolean isHeader) {
    	Label label = new Label(contents);
    	label.getStyleClass().clear();
    	if(isHeader)
    		label.getStyleClass().add("infoTextHeader");
    	else
    		label.getStyleClass().add("infoText");
    	label.setWrapText(true);
    	return label;
    }
    
    private BorderPane configureImage(String url) {
    	Image image = new Image(url);
    	ImageView imageView = new ImageView();
    	imageView.setImage(image);
    	BorderPane pane = new BorderPane();
    	pane.setCenter(imageView);
		return pane;
	}
    /**
     * Returns the contents of the user instructions file.
     * @return The contents of resources/userInstructions.txt
     */
    private String loadInstrctions() {
    	StringBuilder filedata = new StringBuilder();
    	try{
	        Scanner scr = new Scanner(new File("resources/userInstructions.txt"));
	        while(scr.hasNext()){
	        	filedata.append(scr.nextLine() + System.lineSeparator());
	        }
	        scr.close();
	    } catch(FileNotFoundException e){
	    	System.out.println("User instructions could not be located.");
	        System.out.println(e);
	    }
    	return filedata.toString();
    }
    
    /**
     * Constructs a button for closing the view.
     * @return A button that when invoked removes this tab from its tab pane.
     */
    private Button constructCloseButton() {
    	Button close = new Button("Close");
        close.setOnAction(event -> {
            TabPane tabPane = this.getTabPane();
            tabPane.getTabs().remove(this);
        });
        return close;
    }
    
    /**
     * Adds this view to its affiliated tab pane.
     */
    private void addAsTab() {
    	tabPane.getTabs().add(this);
    }
    
    /**
     * Makes this view the selected tab in its affiliated tab pane.
     */
    private void open() {
        tabPane.getSelectionModel().select(this);
    }
    
    String introtxt = 
    		"This text will explain how to use Conan to write proofs.\n";
    	
    String inputtxt = 
    		"In the left-hand menu you will find buttons for all the available rules and relevant logic symbols. "+
    	    "Hovering over the logic symbols will show you a tooltip for text shortcuts. "+
    	    "For all the available shortcuts, check the shortcuts link in the help section, found in the menu. "+
    	    "When writing an expression, you have to use lower case letters for variables and upper case letters for propositions and functions. "+
    	    "If you want more information on the syntax for entering a formula, check the link below, also in the help section.\n\n";

    String conclusiontxt =
    		"In order for the program to be able to verify if you have reached your conclusion, you need to enter the conclusion in the appropriate textfield, as can be seen in the image below. ";
    		
    
    String toggletxt =
    		"In the toolbar you will find checkboxes for toggling verification and generation. "
    		+ "Verification means that the program will verify each step in your proof. "
    		+ "When a rule is incorrectly applied, the rule box turns red and an error message will appear in the bottom left corner. \n\n" +
    	    "Generation means that the program can, in most cases, generate the formula after you apply a rule so that less typing is needed from you. " + 
    	    "You may generate an expression by leaving the expression field empty and inputting valid and verified references." + "\n\n" + 
    	    Constants.forallIntro + " may generate if you type the variable name between " + Constants.forall + " and " + Constants.introduction +  
    	    " (e.g. " + Constants.forall + "x" + Constants.introduction + "). " + 
            "The rules " + Constants.existsIntro + ", " + Constants.equalityIntro + " and " + Constants.equalityElim + " may not generate.\n\n";
                    

    
    String addingrowstxt =
    		"To add a new row after the current one, simply press ENTER. "+
    		"If you're at the end of a box and wish to continue outside of that box, press SHIFT+ENTER. "+
    		"You can find more input options if you follow the link below. ";
    
    String TEXT6 =
    		"";
    
    String TEXT7 =
    		"";
    
    String TEXT8 =
    		"";
    
}