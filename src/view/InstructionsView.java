package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class InstructionsView extends Tab {
    
    private TabPane tabPane;
	
	private final static String tabName = "Instructions";
    
    public InstructionsView(TabPane tabPane) {
    	super(tabName);
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
        Label instructions = constructInstructions();
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
    private Label constructInstructions() {
    	Label instructions = new Label(loadInstrctions());
        instructions.getStyleClass().add("myText");
        instructions.setWrapText(true);
        return instructions;
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