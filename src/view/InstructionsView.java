package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    
    /**
     * Constructs the user instructions.
     */
    private Node constructInstructions() {
    	GridPane instructionGrid = new GridPane();
    	instructionGrid.setVgap(20.0);
    	instructionGrid.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        
        instructionGrid.add(configreLabel("Conan is a tool developed for aiding students learning natural deduction. Because of this, Conan strives to replicate the notation most commonly used in textbooks.\n\n"
    			+ "Presented below are example instructions for proving A∧B ⊢ B∧A (B and A from A and B). Feel free to create a new proof and imitate every step."), 0, 0);
        instructionGrid.add(configreLabel("First, locate the toolbox at the left part of the interface. This tool box provides easy access to frequently used proof components, such as rules of inference and unicode symbols used in logic."), 0, 1);
        instructionGrid.add(configureImage("exampleToolBox.png"), 0, 2);
        instructionGrid.add(configreLabel("Next, fil in the sequent A∧B ⊢ B∧A in the proof header."), 0, 3);
        instructionGrid.add(configureImage("exampleSequent.png"), 0, 4);
        instructionGrid.add(configreLabel("Having filled in the proof header, the next step is to actually construct the proof. This is done by entering one deduction step per row in the proof sheet. Note in particular that each row consists of two or more lines.\n\n"
        		+ "On the first line, the mathematical expression of the deduction step is filled in. On the second, the inference rule is filled in. If the rule is dependent on previous rows, these are filled in on the third and fourth line.\n\n"
        		+ "Pay attention to the colours that indicate that you have reached your conclusion, or if you have made an error."), 0, 5);
        instructionGrid.add(configureImage("exampleProof.png"), 0, 6);
        instructionGrid.add(configreLabel("Congratulations! You have now completed the proof."), 0, 7);
        return instructionGrid;
	}
    
    private Label configreLabel(String contents) {
    	Label label = new Label(contents);
    	label.getStyleClass().add("infoText");
    	label.setWrapText(true);
    	return label;
    }
    
    private ImageView configureImage(String url) {
    	Image image = new Image(url);
    	ImageView imageView = new ImageView();
    	imageView.setImage(image);
		return imageView;
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
}