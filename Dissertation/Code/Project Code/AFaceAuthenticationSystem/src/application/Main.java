package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			Scene scene = new Scene(new StackPane());		
			LoginManager loginManager = new LoginManager(scene, primaryStage);
			loginManager.showLoginScreen();
			this.primaryStage = primaryStage;
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Face Authentication System");
			this.primaryStage.setHeight(700);
			this.primaryStage.setWidth(1000);
			this.primaryStage.show();
			
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

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
