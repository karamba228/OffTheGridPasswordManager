package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.model.Credentials;
import application.model.Encryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CredentialManagerController extends SceneController implements Initializable{
	
	//To be completed
	
	/**
	 * This Observable class array named Credentials will contain an Arraylist of credential objects that will be from'
	 * the encrypted file. This will be the return value for the decrypted method and will be used to display the contents in
	 * a table.
	 */
	private ObservableList<Credentials> products = FXCollections.observableArrayList();
	private  Encryption encryption;
	private Scene scene;
	private String filePath;
	private String filePassword;

	protected CredentialManagerController(Encryption enc, Scene scene) {
		this.encryption = enc;
		this.scene = scene;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		products = encryption.getCredentials();
		filePath = encryption.getAbsolutePath();
		filePassword = encryption.getPassword();
		System.out.println(products.get(0).getNotes()); // for test use
	}
	@Override
	protected void switchToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Main.fxml"));
		Parent root = (Parent)loader.load();
		MainController controller = loader.getController();
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		scene.setRoot(root);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		controller.setScene(scene);
		encryption.encryptFile(filePassword, products, filePath);
		stage.show();
	}
}
