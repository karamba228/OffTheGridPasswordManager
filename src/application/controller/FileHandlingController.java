package application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import application.model.Credentials;
import application.model.Encryption;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/** 4/1/2022
 * This is the main controller that manages the data that is requested in the
 * main menu and also manages the visuals in the main menu.
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales
 *
 */
public class FileHandlingController{ 
	/**
	 * This controller contains all the methods associated with password and will display the dialog boxes
	 */
	private PasswordController passwordController = new PasswordController();
	/**
	 * This will turn true when the file that contains a .ofg extension is found
	 */
	private boolean containsOfg = false;
	/**
	 * This will turn true when the file that contains a .enc extension is found
	 */
	private boolean containsEnc = false;
	/**
	 * This String array will contain the name of the absolute paths of each file in the valid selected directory.
	 */
	private String [] filePath = new String[2];
	/**
	 * This method handles the files that are passed in through the drag and drop feature
	 * or the locate file button. it will check to see if the directory passed only contains a length of two which will then
	 * proceed to check each file in the checkfile method and will return a boolean if the file is valid. After that it will get the
	 * absolute path of each file and will be processed as a string and then passed into the decrypt method for data handling. It will also
	 * contain a password prompt for the user to enter. The encrypt method requires a password to be entered. if the first condition fails, then
	 * it will raise an error, which the user will have to try again.
	 * @param files the specific index of the files
	 * @param key2 
	 * @param circle2 
	 * @return 
	 */
	protected Encryption handleFileDecryption(File files, Circle circle, SVGPath key) {
		Encryption encryption = new Encryption();
		boolean successfulOperation = false;
		if(files.isDirectory() && files.listFiles().length  == 2){
			File[] listOfFiles = files.listFiles();
				
			for (File file : listOfFiles) {
				boolean isCorrectFile = checkTheFile(file);
				if(isCorrectFile == false) {
					circle.setStroke(Color.RED);
					raiseAlert(AlertType.ERROR,"Invalid files","The files in the directory are invalid. "
							+ "They must contain .ofg or .enc",circle,key); 
					containsOfg = false;
					containsEnc = false;
					return null;
				}
			}
			containsOfg = false;
			containsEnc = false;
			while(successfulOperation == false) {
				try {
					successfulOperation = encryption.decryptFile(passwordController.passwordPromptForDecryption(), filePath[1], filePath[0],files.getAbsolutePath());
					if(successfulOperation == false)
						return null;
					circle.setStroke(Color.GREEN);
					key.setStroke(Color.GREEN);
				} catch (Exception e) {
					circle.setStroke(Color.RED);
					key.setStroke(Color.RED);
					raiseAlert(AlertType.ERROR,"Incorrect password or invalid password","Please enter the correct password associated"
							+ "with your files.",circle,key);
				}
			}
		}
		else {
			circle.setStroke(Color.RED);
			key.setStroke(Color.RED);
			raiseAlert(AlertType.ERROR,"Invalid files","The files in the directory are invalid. "
					+ "They must contain .ofg or .enc",circle,key); 
			return null;
		}
		return encryption;
	}
	/**
	 * This method will take it a file from the file List and will go through conditional statements to see if the file
	 * is the correct file. regular expressions are used to check the file extension name. It only will take enc or ofg file extension
	 * @param file is the specific index of the files list data structure and will be processed one at a time
	 * @return it will return whether this file has checked the validity
	 */
	private boolean checkTheFile(File file) {
		if(file.isFile() == true && containsOfg == false || containsEnc == false) {
			if(file.getName().matches(".*\\.ofg")) {
				containsOfg = true;
				filePath[0] = file.getAbsolutePath();
				return containsOfg;
			}
			else if(file.getName().matches(".*\\.enc")) {
				containsEnc = true;
				filePath[1] = file.getAbsolutePath();
				return containsEnc;
			}
		}
		return false;
	} 
	/**
	 * This method is responsible for displaying the type of alert, header, and context of an alert.
	 * it will also set the color of the key circle to a neutral color previously being red for an error.
	 * @param alertType the type of alert the user wants to display
	 * @param header the header of the text
	 * @param content whatever the context will be of the alert
	 */
	private void raiseAlert(AlertType alertType,String header, String content,Circle circle, SVGPath Key) {
		Alert alert = new Alert(alertType);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		alert.setOnCloseRequest((e -> {circle.setStroke(Color.GRAY);Key.setStroke(Color.GRAY);}));	
	}
	/**
	 * This method will open up the file manager and ask the user where to save their folder at. It will
	 * return the absolute path for desired location.
	 * @param foldername the name of the folder the user wanted to name it
	 * @param node any node that is passed, preferably a stage
	 * @return it returns an absolute path
	 * @throws IOException in case of a fatal error
	 */
	private String saveFolder(Node node) throws IOException {
			Stage stage = (Stage) node.getScene().getWindow();
    		FileChooser chooser = new FileChooser();
        	chooser.setTitle("Select location to save folder");
        	File selectedFile = chooser.showSaveDialog(stage);
        	if (selectedFile != null) {
        		Path path = Paths.get(selectedFile.getAbsolutePath());
        		Files.createDirectories(path);
        		return path.toString();
        	}
        return null;
	}
	/**
	 * this method will gather all the inputed data and combine it in the encrypt method to encrypt the data 
	 * the user wanted
	 * @param node this node contains the stage
	 * @param credentials the credentials the user inputed
	 * @throws IOException in case of an error
	 */
	protected void handleFileEncryptionCreation(Node node, List<Credentials> credentials) throws IOException{
		Encryption encryption = new Encryption();
		String absolutePath = saveFolder(node);
		System.out.println(absolutePath);
		if(absolutePath == null)
				return;
		else {
				encryption.encryptFile(passwordController.passwordPromptForEncryption("New Password","Enter new password "
						+ "for file encryption"), credentials, absolutePath);
		}
	}
}
