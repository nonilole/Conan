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
            Image image = new Image("inferenceRules.jpg");

            ImageView iv1 = new ImageView();
            iv1.setImage(image);
            iv1.setSmooth(true);
            iv1.setPreserveRatio(true);

            //current window size determines the scaling
            // iv1.fitHeightProperty().bind(tabPane.heightProperty());
            // iv1.fitWidthProperty().bind(tabPane.widthProperty());

            //putting the image on a scrollpane
            ScrollPane sp=new ScrollPane();
            tab = new ViewTab("Inference Rules",this);
            sp.setContent(iv1);
            tabPane.getTabs().add(tab);
            tab.setContent(sp);
            tabPane.getSelectionModel().select(tab);


            int widthOfToolbar=235; //note to self: breaks when changing toolbar size


            //Fit the image with the tabpane by calculating screen width - width of toolbar while preserving ratio
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            double w=primaryScreenBounds.getWidth()-widthOfToolbar;
            iv1.setFitWidth(w);
       }

    @Override
    public ViewTab getTab() {
        return tab;
    }
}
