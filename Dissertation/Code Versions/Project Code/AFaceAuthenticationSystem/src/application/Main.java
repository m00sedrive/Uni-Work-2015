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
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FXFaceAuth.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("A Face Authentication System");
			primaryStage.setHeight(700);
			primaryStage.setWidth(900);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args) {
	
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
