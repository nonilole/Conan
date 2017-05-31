package start;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {
	
	private Stage mainStage;

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sketch.fxml"));
		Scene scene = new Scene(root, 1200, 700);
		Preferences prefs = Preferences.userRoot().node("General");
		switch (prefs.getInt("theme",0)) {
		case 0:
			scene.getStylesheets().add("minimalistStyle.css");
			break;
		case 1:
			scene.getStylesheets().add("gruvjan.css");
			break;
		}
		stage.setTitle("Conan");
		stage.getIcons().add(new Image("icon.png"));
		stage.setScene(scene);
		stage.show();
		
		stage.setOnCloseRequest(confirmCloseEventHandler);
		
		//Button closeButton = new Button("Close Application");
		//closeButton.setOnAction(event ->
		  //      stage.fireEvent( new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
	}
	
	private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "You may have unsaved proofs,\nare you sure you want to exit?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(mainStage);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };
	
	public static void main(String[] args) {
		launch(args);
	}

}
