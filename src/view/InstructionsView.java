package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class InstructionsView extends Tab {
    
	private GridPane gridPane;
    private ScrollPane scrollPane;
    AnchorPane ap;
	
	private final static String tabName = "Instructions";
    private Label heading;
    private Label instructions;
    Button close;

    /**
     * Sets the layout of the component
     */
    private void setLayout() {
        gridPane = new GridPane();
        gridPane.setVgap(20.0);
        gridPane.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        
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

        gridPane.getRowConstraints().addAll(rowC1, rowC2, rowC3, rowC4, rowC5);
	}
    
    /**
     * Enables scrolling in the view.
     */
    private void makeScrollable() {
    	scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }
    
    /**
     * Anchors the instruction view to its content.
     */
    private void anchor() {
    	 ap = new AnchorPane(scrollPane);
         AnchorPane.setTopAnchor(scrollPane, 0.0);
         AnchorPane.setRightAnchor(scrollPane, 0.0);
         AnchorPane.setBottomAnchor(scrollPane, 0.0);
         AnchorPane.setLeftAnchor(scrollPane, 0.0);
    }
    
    /**
     * Constructs the view heading.
     */
    private void setupHeading() {
    	final String text = "How to use Conan";
    	heading = new Label(text);
    	heading.getStyleClass().add("myTitle");
    	gridPane.add(heading, 0, 0);
    }
    
    /**
     * Constructs the user instructions.
     */
    private void setupInstructions() {
    	final String body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ante est, placerat quis ultricies sed, varius nec ante. Nullam interdum eu tortor quis faucibus. Duis accumsan purus ac diam porta, eget semper arcu pulvinar. Sed et ornare ligula. Morbi sollicitudin pretium eros, vitae feugiat ante tincidunt vel. Cras aliquam lobortis neque, non rutrum orci volutpat et. Nam at tincidunt mauris. Phasellus lobortis lorem dolor, sit amet tempus nulla pellentesque nec. Aenean a venenatis lectus.";
    	instructions = new Label(body);
        instructions.getStyleClass().add("myText");
        instructions.setWrapText(true);
        gridPane.add(instructions, 0, 2);
	}
    
    private void setupCloseButton(){
    	close = new Button("Close");
        close.setOnAction(event -> {
            TabPane tabPane = this.getTabPane();
            tabPane.getTabs().remove(this);
        });
        gridPane.add(close, 0, 4);
    }
    
    /**
     * Handles the alignment of components.
     */
    private void alignContents() {
    	GridPane.setHalignment(heading, HPos.CENTER);
    	GridPane.setValignment(heading, VPos.CENTER);
    	GridPane.setHalignment(close, HPos.RIGHT);
	}
    
    /**
     * Adds this view to the provided 
     * @param tabPane
     */
    private void addAsTab(TabPane tabPane) {
    	tabPane.getTabs().add(this);
    }
    
    private void open(TabPane tabPane) {
        tabPane.getSelectionModel().select(this);
    }
    
    public InstructionsView(TabPane tabPane) {
    	super(tabName);
        setLayout();
        makeScrollable();
        anchor();
        setupHeading();
        setupInstructions();
        setupCloseButton();
        alignContents();
        
        this.setContent(ap);
        
        addAsTab(tabPane);
        open(tabPane);
    }
}

