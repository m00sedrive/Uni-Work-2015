package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import database.Database;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/** Controls the main application screen */
public class TrainingController extends AppTools {

	@FXML private Button add_images;
	@FXML private Button loadDatabase;
	@FXML private Button logoutButton;
	@FXML private Button returnToMainScreen;
	@FXML private Button selectImageButton;
	@FXML private Button harrysPCA;
	@FXML private Button searchPersonButton;
	@FXML private Button writeDatabase;
	@FXML private GridPane image_grid;
	@FXML private ImageView selectedImageView;
	@FXML private VBox vBox_right;
	@FXML private HBox hBox_imgGallery_r1;
	@FXML private HBox hBox_imgGallery_r2;
	@FXML private HBox hBox_imgGallery_r3;
	@FXML private TextArea textAreaTrain;

	public Database database;
	public EigenCache cache;
	public CustomPCA cpca;
	private Image imageSelection;
	private boolean trainedDataWritten = false;

	public void initSessionID(final LoginManager loginManager, String sessionID) {

		textAreaTrain.setEditable(false);
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.logout();
			}
		});
		returnToMainScreen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.authenticated(sessionID);
			}
		});
	}
	
	@FXML
	public void loadDatabase() {
		textAreaTrain.setText("Loading Database...");
		database = new Database();
		// set up database
		database.setUpDatabase();
		textAreaTrain.setText("Database loaded");

	}

	@FXML
	public void trainImages() {
		if (database != null) {
			textAreaTrain.setText("Training Database...");
			cpca = new CustomPCA();
			if (database != null) {
				cpca.setPCAData(database);
				cpca.prepareFaceMatrix();
				cpca.performPCA();
				textAreaTrain.setText("Training Complete!");
			} else {
				// display to user database not set
				textAreaTrain.setText("No database detected:  Please load selected database!");
			}
			// set eigen cache with PCA results
			cache = cpca.getPCAResults();
		} else {
			textAreaTrain.setText("Database has not been loaded." + "\n" + "Please load a database to train!");
		}
	}

	@FXML
	public void writeDatabase() throws IOException {
		if (database != null && cache != null) {
			FileOutputStream fs = new FileOutputStream(
					new File("C:\\Users\\user\\Desktop\\FAResults\\EigenCache\\eigenCache.db"));
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(cache);
			os.close();
			fs.close();
			textAreaTrain.setText("Write database to file complete!");
			trainedDataWritten = true;
		} else {
			textAreaTrain
					.setText("No trained database data detected." + "\n" + "Please train the selected database first!");
		}
	}

	@FXML
	public void selectImage() throws IOException {

		// set selected image view with file chooser selection
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select person image to search.");
		final File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			imageSelection = new Image("file:///" + selectedFile.getPath());
			selectedImageView.setImage(imageSelection);
		}
	}

	@FXML
	public void searchForPerson() {

		if(imageSelection != null && trainedDataWritten) {
			// get detected face and convert to buffered image.
			BufferedImage image = SwingFXUtils.fromFXImage(imageSelection, null);
			// search for image in eigen cache results
			SearchResults[] results;
			SearchPerson personSearch = new SearchPerson(cpca.getPCAResults(), image, 250);
			results = personSearch.searchPersonInCache();
		}
		else {
			textAreaTrain.setText("No written training data detected." + "\n" + "Please write trained database before searching for person.");
		}
	}

	@FXML
	public void testAlternativePca() {
		API.PCA.PCA pca = new API.PCA.PCA();
		pca.image_data = CustomPCA.getDebugMatrix();
		pca.run();
	}
}