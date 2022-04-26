package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** 4/1/2022
 * This is the Credentials class that process each credentials that is passed through the construct
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales, Eduardo Riveragarza, Igor Derke
 *
 */
public class Credentials {
	
	private SimpleStringProperty email;
    private SimpleStringProperty password;
	private SimpleStringProperty url;
	private SimpleStringProperty notes;
	
	private ObservableList<Credentials> credentials = FXCollections.observableArrayList();
	/**
	 * This is a constructor that takes in each credentials and processes it into an object
	 * @param email of the account
	 * @param password of the account
	 * @param url of the website
	 * @param notes any notes to use when using that account
	 */

	public Credentials(String email, String password, String url, String notes) {
		this.email = new SimpleStringProperty(email.replace(',', (char) 169));
		this.password =  new SimpleStringProperty(password.replace(',', (char) 169));
		this.url =  new SimpleStringProperty(url.replace(',', (char) 169));
		this.notes =  new SimpleStringProperty(notes.replace(',', (char) 169));
	}
	
	public Credentials() {}

	/**
	 * This method gets the username or email of the selected object
	 * @return the email of the account
	 */
	public String getEmail() {
		if(email.get().matches(""))
			return "N/A";
		return email.get();
	}
	
	/**
	 * This sets a new email or username of the selected object
	 * @return the new email or username of the account
	 */
	public void setEmail(String email) {
		
		this.email.set(email);
	}

	/**
	 * This method gets the password of the selected object
	 * @return the password of the account
	 */
	
	public String getPassword() {
		if(password.get().matches(""))
			return "N/A";
		return password.get();
	}
	/**
	 * This sets a new password of the selected object
	 * @return the new password of the account
	 */
	
	public void setPassword(String password) {
		this.password.set(password);
	}
	/**
	 * This method gets the url of the selected object
	 * @return the url of the website
	 */
	public String getUrl() {
		if(url.get().matches(""))
			return "N/A";
		return url.get();
	}
	/**
	 * This sets a new URL of the selected object
	 * @return the new URL of the account
	 */
	
	public void setUrl(String url) {
		this.url.set(url);
	}
	/**
	 * This method gets the notes of the selected object
	 * @return the notes to note about the account
	 */
	public String getNotes() {
		if(notes.get().matches(""))
			return "N/A";
		return notes.get();
	}
	/**
	 * This sets a new note of the selected object
	 * @return the new notes of the account
	 */
	public void setNotes(String notes) {
		this.notes.set(notes);
	}
	/**
	 * These methods are used whenever decrypting the file and trying to retrieve the
	 * credentials contents. The one above are used to return the credentials and also
	 * validate if the are empty or not.
	 * 
	 */
	
	public StringProperty emailProperty() {
		return email;
	}
	public StringProperty passwordProperty() {
		return password;
	}
	public StringProperty urlProperty() {
		return url;
	}
	public StringProperty notesProperty() {
		return notes;
	}

	public ObservableList<Credentials> getCredentials() {
		return credentials;
	}

	public void setCredentials(ObservableList<Credentials> credentials) {
		this.credentials = credentials;
	}
}
