package application;

import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import org.eclipse.fx.ui.mobile.MobileApp;
import java.util.Collections;
import java.util.List;
import org.eclipse.fx.core.fxml.ExtendedFXMLLoader;
import java.io.IOException;

public class Main extends MobileApp {

	public Region createUI() {
		try {
			BorderPane root = (BorderPane)new ExtendedFXMLLoader().load(getClass().getClassLoader(),"application/FXMobEdgeDetec.fxml");
			return root;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getInitialStylesheets() {
		return Collections.singletonList(getClass().getResource("application.css").toExternalForm());
	}
}
