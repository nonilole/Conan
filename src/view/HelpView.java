package view;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


class HelpView implements View {
    //The tab object of this view
    private ViewTab tab;

    /**
     * Adds the content for showing the inference rules to the TabPane in the proof
     * @param tabPane
     *
     */
    public HelpView(TabPane tabPane) {
    //TODO adjust the image based on screen size

            //load the image
            Image image = new Image("inferenceRules.jpg");

            //image resizing
            ImageView iv1 = new ImageView();
            iv1.setImage(image);
            iv1.setSmooth(true);
            iv1.fitHeightProperty().bind(tabPane.heightProperty());
            iv1.fitWidthProperty().bind(tabPane.widthProperty());

            //putting the image on a scrollpane
            ScrollPane sp=new ScrollPane();
            tab = new ViewTab("Inference Rules",this);
            sp.setContent(iv1);
            tabPane.getTabs().add(tab);
            tab.setContent(sp);
            tabPane.getSelectionModel().select(tab);

    }

    @Override
    public ViewTab getTab() {
        return tab;
    }
}
