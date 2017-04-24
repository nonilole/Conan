package start;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
 
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sketch.fxml"));
        Scene scene = new Scene(root, 1200, 600);
        scene.getStylesheets().add("minimalistStyle.css");
//        scene.getStylesheets().add("gruvjan.css");
        stage.setTitle("Conan");
        stage.getIcons().add(new Image("./icon.png"));
        stage.setScene(scene);
        stage.show();
    }
 public static void main(String[] args) {
        launch(args);
    }
}
