package application.controller;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.Credentials;
import application.model.Encryption;
import application.model.Preferences;
import application.model.Settings;
import application.view.Animations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
/** 4/24/2022
 * This is the CredentialManager controller that manages the data that is requested in the
 * Credential List menu and also manages the visuals in the Credential List menu.
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales, Eduardo Riveragarza, Igor Derke
 *
 */
public class CredentialManagerController extends SceneController implements Initializable{
	
	@FXML
	private BorderPane mainPane;
	
	/**
	 * Definition for the FXML ListView component of type Credentials
	 */
	@FXML
	private ListView<Credentials> passwordList = new ListView<>();
	
	/**
	 * Definition for the FXML ListView component of type String
	 */
	@FXML
	private ListView<String> detailView = new ListView<>();
	
	/**
	 * Initialize FHController for file updating
	 */
	private FileHandlingController fileHandlingController;	
	
	/**
	 * Define all user accessible TextFields
	 */
	@FXML
	private TextField editField, userIDInput, passwordInput, urlInput, notesInput;
	
	/**
	 * Define all user accessible TextFields
	 */
	@FXML
	private Button mainMenu, edit, delete, save, add, addSubmit;
	
	/**
	 * The circle will be set to a different color and will have an animation
	 */
	@FXML
	private Circle circle, circleEdit, circleDelete, circleSave, circleAdd;
	
	/**
	 * This shape is behind the continuePrompt button to give its shape. It also plays an animation when hovered
	 */
	@FXML
	private Rectangle rectangleButtonShape;
	
	/**
	 * This class will call any custom animation that wants to be played
	 */
	private Animations animation;
	
	private Settings settings;
	
	/**
	 * Variables for tracking list selected items
	 */
	private Credentials selected;
	private Integer selectedID;
	private Integer detailSelected;
		
	
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

	/**
	 * Constructor
	 */
	protected CredentialManagerController(Encryption enc, Scene scene) {
		this.encryption = enc;
		this.scene = scene;
	}
	
	/**
	 *This method will be called the moment the scene is transitioned and will get all the necessary classes and set
	 *up the stage for use.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		products = encryption.getCredentials();
		filePath = encryption.getAbsolutePath();
		filePassword = encryption.getPassword();
		fileHandlingController = new FileHandlingController();
		
		for (int i = 0; i < products.size(); i++) {
			passwordList.getItems().add(products.get(i));
		}
		
		passwordList.setCellFactory(param -> new PasswordCell());
		detailView.setCellFactory(param -> new DetailCell());
		
		try {
        	animation = new Animations();
			settings = Preferences.loadPreferences();
			animation.setAnimationStatus(settings.isDisableAnimation());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * This method will be called whenever the user presses the main button. It will
	 * take the user back to the main menu
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
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
		stage.show();	
	}
	
	/**
	 * This method will be called whenever the user presses the Edit button.
	 * It will check selected and detailSelected to show a TextField (editField) for editing a value
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void editCredentials(ActionEvent event) throws IOException {
		if (selected != null && detailSelected != null) {
			editField.setVisible(true);
		}
	}
	
	/**
	 * This method will be called whenever the user submits a change by pressing Enter
	 * Updates selected item from the list and Updates all related lists
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void edditSubmit(ActionEvent event) throws IOException {	
		if (detailSelected == 0) {
			selected.setEmail(editField.getText());
		} else {
			selected.setPassword(editField.getText());
		}
		
		products.set(selectedID, selected);
		
		detailView.getItems().clear();
    	detailView.getItems().add("login: "+selected.getEmail());
    	detailView.getItems().add("password: "+selected.getPassword());
		
    	editField.setVisible(false);
	}
	
	/**
	 * This method will be called whenever the user presses the Add button.
	 * Makes 4 input fields visible for adding a password to the list
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void addCredentials(ActionEvent event) throws IOException {
		userIDInput.isVisible();
		userIDInput.setVisible(!userIDInput.isVisible());
		passwordInput.setVisible(!passwordInput.isVisible());
		urlInput.setVisible(!urlInput.isVisible());
		notesInput.setVisible(!notesInput.isVisible());
		addSubmit.setVisible(!addSubmit.isVisible());
	}
	
	/**
	 * Adds a new Credential to the list of credentials.
	 * Checks every input field and alerts if it's empty
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void addSubmit(ActionEvent event) throws IOException {	
		if(userIDInput.getText() == "" && passwordInput.getText() == "" && urlInput.getText() == "" && notesInput.getText() == "") {
			Alert alert = new Alert(AlertType.WARNING, "You must enter credentials, try again", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		// Adds Credentials to each list
		passwordList.getItems().add(new Credentials(userIDInput.getText(),passwordInput.getText(),urlInput.getText(),notesInput.getText()));
		products.add(new Credentials(userIDInput.getText(),passwordInput.getText(),urlInput.getText(),notesInput.getText()));
		
		// Clear input fields
		userIDInput.clear();
		passwordInput.clear();
		urlInput.clear();
		notesInput.clear();
		
		// Hide input fields
		userIDInput.setVisible(false);
		passwordInput.setVisible(false);
		urlInput.setVisible(false);
		notesInput.setVisible(false);
		addSubmit.setVisible(false);
	}
	
	/**
	 * This method will be called whenever the user presses the main button. It will
	 * take the user back to the main menu
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void deleteCreadentials(ActionEvent event) throws IOException {
		passwordList.getItems().remove(selected);
		products.remove(selected);
		detailView.getItems().clear();
	}
	
	/**
	 * This method will be called whenever the user presses the Save button.
	 * Submits final list of Credentials to be encrypted and stored
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	protected void saveChanges(ActionEvent event) throws IOException {
		fileHandlingController.updateFile(filePath, filePassword, products);
	}
	
	/**
	 * This method detects when the mouse entered one of the three smaller circles that
	 * contain the buttons. it will proceed to play the circle rotation animation and will 
	 * play the stroke color fill animation aka the fillAnimation method.
	 * @param event this event is when the mouse enters the circle diameter
	 */
	@FXML 
	private void mouseEnteredButton(MouseEvent event){
		if(event.getSource() == mainMenu) {
			animation.playRotationAnimation(circle,false,360,0.5,1,1);
			animation.fillAnimation(circle,Color.valueOf(settings.getColorMainMenuForNewFile()));
		}
		else if(event.getSource() == edit) {
			animation.playRotationAnimation(circleEdit,false,-360,0.5,1,1);
			animation.fillAnimation(circleEdit,Color.LIGHTYELLOW);
		}
		else if(event.getSource() == delete) {
			animation.playRotationAnimation(circleDelete,false,-360,0.5,1,1);
			animation.fillAnimation(circleDelete,Color.RED);
		}
		else if(event.getSource() == save) {
			animation.playRotationAnimation(circleSave,false,-360,0.5,1,1);
			animation.fillAnimation(circleSave,Color.GREEN);
		}
		else if(event.getSource() == add) {
			animation.playRotationAnimation(circleAdd,false,-360,0.5,1,1);
			animation.fillAnimation(circleAdd,Color.GREEN);
		}
	}
	
	/**
	 * this method detects when the mouse exits the button that is in the circle.(The mouse and circle
	 * basically match the same diameter, but technically its the button that contains the action). It will 
	 * proceed to play the circle rotation animation but in reverse,and will set the color to be grey to notify that
	 * the mouse is not in the area of the circle anymore.
	 * @param event this event is when the mouse exists the circle diameter
	 */
	@FXML
	private void mouseExitedButton(MouseEvent event){
		if(event.getSource() == mainMenu) {
			animation.playRotationAnimation(circle,true,-360,0.5,1,1);
			animation.fillAnimation(circle,Color.GRAY);
		}
		else if(event.getSource() == edit) {
			animation.playRotationAnimation(circleEdit,false,-360,0.5,1,1);
			animation.fillAnimation(circleEdit,Color.GRAY);
		}
		else if(event.getSource() == delete) {
			animation.playRotationAnimation(circleDelete,false,-360,0.5,1,1);
			animation.fillAnimation(circleDelete,Color.GRAY);
		}
		else if(event.getSource() == save) {
			animation.playRotationAnimation(circleSave,false,-360,0.5,1,1);
			animation.fillAnimation(circleSave,Color.GRAY);
		}
		else if(event.getSource() == add) {
			animation.playRotationAnimation(circleAdd,false,-360,0.5,1,1);
			animation.fillAnimation(circleAdd,Color.GRAY);
		}
	}
	
	/**
	 * PasswordCell is a cellFactory for the listView of credentials
	 */
	private class PasswordCell extends ListCell<Credentials> {
        @Override
        public void updateItem(Credentials obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
            	setText(null);
            } else {
            	setText(obj.getUrl());
            	setItem(obj);
            	
            	// When a cell is selected, data gets passet to detailView list with credential details
            	super.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (event.getButton()== MouseButton.PRIMARY && (! super.isEmpty())) {
                    	selectedID = super.getIndex();
                    	Credentials item = super.getItem();
                    	selected = item;
                    	detailView.getItems().clear();
                    	detailView.getItems().add("login: "+item.getEmail());
                    	detailView.getItems().add("password: "+item.getPassword());
                    }
                });
            }
        }
    }
	
	/**
	 * DetailCell is a cellFactory for the listView of String
	 */
	private class DetailCell extends ListCell<String> {
		/**
		 * updateItem is a ListView cell handler. Detects when cell is clicked and assigns a 
		 * number (0 or 1) (0 --> login is selected), (1 --> password is selected)
		 * @param obj - Object assigned to a ListView Cell
		 * @param empty - A boolean value specifying wether the cell is empty
		 */
        @Override
        public void updateItem(String obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
            	setText(null);
            } else {
            	setText(obj);
            	
            	super.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (event.getButton()== MouseButton.PRIMARY) {
                    	detailSelected = super.getIndex();
                		if (detailSelected == 0) {
                			editField.setText(selected.getEmail());
                		} else {
                			editField.setText(selected.getPassword());
                		}
                    }
                });
            }
        }
    }
}
