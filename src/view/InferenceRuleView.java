package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;


public class InferenceRuleView implements View {
    //The tab object of this view
    private ViewTab tab;

    /**
     * Adds the content for showing the inference rules to the TabPane in the proof
     * @param tabPane
     *
     */
    public InferenceRuleView(TabPane tabPane) {


        //load the image
        Image image = new Image("inferenceRules.png");
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setSmooth(true);
        iv1.setPreserveRatio(true);

        //putting the image on a scrollpane
        ScrollPane sp=new ScrollPane();
        sp.getStyleClass().add("rulesView");
        tab = new ViewTab("Inference Rules",this);
        sp.setContent(iv1);
        tabPane.getTabs().add(tab);
        tab.setContent(sp);
        tabPane.getSelectionModel().select(tab);

        //used for getting screensize
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //Avoids scaling too much
        double w=primaryScreenBounds.getWidth()/2;
        iv1.setFitWidth(w);
    }

    @Override
    public ViewTab getTab() {
        return tab;
    }

    @Override
    public void focusFirst() {

    }
}
