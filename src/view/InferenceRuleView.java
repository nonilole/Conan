package view;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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

            //image resizing
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

            //Fit the width of the image with the width of the tabpane while preserving the image ratio
            double w=tab.getView().getTab().getTabPane().getMaxWidth();
            iv1.setFitWidth(w);

    }

    @Override
    public ViewTab getTab() {
        return tab;
    }
}
