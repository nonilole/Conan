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

        //scales based on current screen size
        iv1.fitHeightProperty().bind(tabPane.heightProperty());
        iv1.fitWidthProperty().bind(tabPane.widthProperty());

        tab = new ViewTab("Inference Rules",this);
        tab.setContent(iv1);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @Override
    public ViewTab getTab() {
        return tab;
    }
}
