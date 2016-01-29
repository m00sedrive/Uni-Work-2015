package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
				
		try {
			Scene scene = new Scene(new StackPane());
			
			LoginManager loginManager = new LoginManager(scene);
			loginManager.showLoginScreen();
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Face Authentication System");
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
