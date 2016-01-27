package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
            try {
                    // load the FXML resource
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstJFX.fxml"));
                    // store the root element so that the controllers can use it
                    BorderPane root = (BorderPane) loader.load();
                    // create and style a scene
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                    // create the stage with the given title and the previously created scene
                    primaryStage.setTitle("JavaFX OpenCV Test");
                    primaryStage.setScene(scene);
                    // show the GUI
                    primaryStage.show();
                    // set a reference of this class for its controller
                    FXController controller = loader.getController();
                    controller.setRootElement(root);

            } catch(Exception e) {
                    e.printStackTrace();
            }
    }

    public static void main(String[] args) {
            // load the native OpenCV library
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            launch(args);
    }
}