package application.controller;

import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

public class PasswordController {
	
	/**
	 * This method prompts the user to enter a password to continue with the encryption process. It will ask the user to re-enter it
	 * as well so  the password they want is correct. If they press cancel the whole process will halt
	 * @param title a title for the dialog box
	 * @param header the context for the box
	 * @return
	 */
	public String passwordPromptForEncryption(String title, String header) {
	    Dialog<String> dialog = new Dialog<>();
	    SVGPath svg = new SVGPath();
	    dialog.setTitle(title);
	    dialog.setHeaderText(header);
	    svg.setContent("M 40 25 A 5 5 0 1 1 45 25 L 50 40 L 35 40 Z");
	    dialog.setGraphic(svg);
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	    PasswordField password = new PasswordField();
	    PasswordField password2 = new PasswordField();
	    HBox content = new HBox();
	    content.setAlignment(Pos.CENTER_LEFT);
	    content.setSpacing(10);
	    content.getChildren().addAll(new Label("Password:"), password ,new Label("Re-Enter Password: "),password2);
	    dialog.getDialogPane().setContent(content);
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == ButtonType.OK) {
	           return password.getText();
	        }
	        return "../";
	    });
	    Optional<String> result = dialog.showAndWait();
	    if(result.get() == "../")
	    	return null;
	    
	    while(!password.getText().equals(password2.getText())||password.getText() == "" && password2.getText() == "") {
	    	Optional.empty();
	    	dialog.setResultConverter(dialogButton -> {
		    	 if (dialogButton == ButtonType.OK) {
			           return password.getText();
			        }
			        return "../";
			    });
	    	result = dialog.showAndWait();
	    	if(result.get() == "../")
		    	return null;
	    }
		return result.get();
	}
	
	/**
	 * This method display a popup box that asks for a password. It uses the Passwordfield to protect the 
	 * Inputed password for security. It displays a logo and the text prompt for the user to enter the password.
	 * @return it returns the result of the inputed password the user entered from the dialog box
	 */
	public String passwordPromptForDecryption() {
	    Dialog<String> dialog = new Dialog<>();
	    SVGPath svg = new SVGPath();
	    dialog.setTitle("Password for Encrypted file");
	    dialog.setHeaderText("Please enter password for your folder.");
	    svg.setContent("M 40 25 A 5 5 0 1 1 45 25 L 50 40 L 35 40 Z");
	    dialog.setGraphic(svg);
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	    dialog.setX(800);
	    dialog.setY(500);

	    PasswordField password = new PasswordField();
	    HBox content = new HBox();
	    content.setAlignment(Pos.CENTER_LEFT);
	    content.setSpacing(10);
	    content.getChildren().addAll(new Label("Password:"), password);
	    dialog.getDialogPane().setContent(content);
	    dialog.setResultConverter(dialogButton -> {
	    	 if (dialogButton == ButtonType.OK) {
		           return password.getText();
		        }
		        return "../";
		    });
		    Optional<String> result = dialog.showAndWait();
		    if(result.get() == "../")
		    	return null;
		    
		    while(password.getText().equals("")) {
		    	Optional.empty();
		    	dialog.setResultConverter(dialogButton -> {
			    	 if (dialogButton == ButtonType.OK) {
				           return password.getText();
				        }
				        return "../";
				    });
		    	result = dialog.showAndWait();
		    	if(result.get() == "../")
			    	return null;
		    }
			return result.get();
	}

}
