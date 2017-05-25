package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;


public class ShortcutsView implements View {
    //The tab object of this view
    private ViewTab tab;

    /**
     * Adds the content for showing the shortcuts to the TabPane in the proof
     * @param tabPane
     *
     */
    public ShortcutsView(TabPane tabPane) {

    	Label label = new Label(loadInstructions());
    	label.getStyleClass().add("infoText");

        //putting the image on a scrollpane
        ScrollPane sp = new ScrollPane();
        sp.getStyleClass().add("shortcutsView");
        tab = new ViewTab("Shortcuts",this);
        sp.setContent(label);
        tabPane.getTabs().add(tab);
        tab.setContent(sp);
        tabPane.getSelectionModel().select(tab);

        //used for getting screensize
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //Avoids scaling too much
        double w=primaryScreenBounds.getWidth();
        label.setPrefWidth(w); 
    }
    
    /**
     * Returns the contents of the user instructions file.
     * @return The contents of resources/userInstructions.txt
     */
    private String loadInstructions() {
    	StringBuilder filedata = new StringBuilder();
    	try{
	        Scanner scr = new Scanner(getClass().getResourceAsStream("/shortcuts.txt"));
	        while(scr.hasNext()){
	        	filedata.append(scr.nextLine() + System.lineSeparator());
	        }
	        scr.close();
	    } catch(/*FileNotFound*/Exception e){
	    	System.out.println("User instructions could not be located.");
	        System.out.println(e);
	    }
    	return filedata.toString();
    }

    @Override
    public ViewTab getTab() {
        return tab;
    }

    @Override
    public void focusFirst() {

    }
}

