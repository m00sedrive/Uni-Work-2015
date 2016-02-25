package application;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)
    {
            try
            {
                    // load the FXML resource
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FD.fxml"));
                    BorderPane root = (BorderPane) loader.load();
                    // set a whitesmoke background
                    root.setStyle("-fx-background-color: whitesmoke;");
                    // create and style a scene
                    Scene scene = new Scene(root, 800, 600);
                    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                    // create the stage with the given title and the previously created
                    // scene
                    primaryStage.setTitle("Face Detection");
                    primaryStage.setScene(scene);
                    // show the GUI
                    primaryStage.show();

                    // init the controller
                    FDController controller = loader.getController();
                    controller.init();
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
    }

    public static void main(String[] args)
    {
            // load the native OpenCV library
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            launch(args);
    }
}