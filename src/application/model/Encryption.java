package application.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/** 4/1/2022
 * This is the Encrpytion class that takes care of processing the selected files and contents in the files and either encrypts
 * that data or decrypts for use. The encryption and decryption uses a password to lock and unlock the created file with credentials
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales, Eduardo Riveragarza
 *
 */
public class Encryption {
		private ObservableList<Credentials> credentials = FXCollections.observableArrayList();
		private String password;
		private String absolutePath;
	/**
	 * This method takes in the chosen password and converts it into a char array to then bytes to be 
	 * re used later to unlock the file. A salt and iv file is created to store that password and will also be 
	 * created alongside that actual encrypted file. The hashing algorithm in use is the SHA256 alongside AES
	 * @param password the typed password that was prompted
	 * @param credentials the contents of data that you want encrypted
	 * @param destination the selected destination the user wants the folder to be stored at
	 */
	public void encryptFile(String password,List<Credentials> credentials,String destination){
		
		if(password == null)
			return;
		
		String [] arrayCredentials = new String[4 * credentials.size()];
		short k = 0;
		for(short i = 0; i < credentials.size(); i++) {
				arrayCredentials[k] = credentials.get(i).getEmail()+",";
				k++;
				arrayCredentials[k] = credentials.get(i).getPassword()+",";
				k++;
				arrayCredentials[k] = credentials.get(i).getUrl()+",";
				k++;
				arrayCredentials[k] = credentials.get(i).getNotes()+",";
				k++;
		}
		credentials.clear();
		try {
		FileOutputStream output = new FileOutputStream(destination+"/encryptedfile.ofg");
		byte[] salt = new byte[8];

		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,256);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();

		FileOutputStream ivOutFile = new FileOutputStream(destination+"/iv.enc");
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		ivOutFile.write(salt);
		ivOutFile.write(iv);
		ivOutFile.close();
		
		processFile(cipher,output,amountOfBytesInArray(arrayCredentials),arrayCredentials);
		arrayCredentials = null;
		Alert alert = new Alert(AlertType.INFORMATION,"You have successfully created your file. Please make a backup of your file to prevent any unexpected data loss.", ButtonType.OK);
		alert.showAndWait();
		}catch(Exception e) {
			System.out.println("Something went wrong, try again");
		}
	}
	/**
	 * This method will decrypt the chosen folder with the file contents that were encrypted. It will take
	 * in the chosen password and will make sure it's valid with the current input. the salt
	 * and iv are read in byte form which, contains the key and will be processed along side the password.
	 * If everything checks, the cipher will get passed into the fileContents method, which will be evaluated
	 * in bytes and converted into an string.
	 * 
	 * @param password the chosen password that was initially made up in the encrypted method
	 * @param enc the enc file that contains the salt and iv
	 * @param ofg the actual data that contains the credentials
	 * @param absolutePath 
	 * @return 
	 * @return returns an ObservableList object array to be used for a table.
	 * @throws Exception incase a fatal error occurred
	 */
	public boolean decryptFile(String password,String enc, String ofg, String absolutePath) throws Exception{
		if(password == null)
			return false;
		FileInputStream in = new FileInputStream(enc);
		byte[] salt = new byte[8], iv = new byte[128/8];
		in.read(salt);
		in.read(iv);
		in.close();
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,256);
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		FileInputStream input = new FileInputStream(ofg);
		fileContents(cipher, input, amountOfBytesInFile(input));
		setPassword(password);
		setAbsolutePath(absolutePath);
		return true;
	}
	/**
	 * This method gets the encrypted file and determined the amount of bytes that are in it. It 
	 * does this so it does have any issues when allocation takes place. Not enough space could cause issues and
	 * too much is wasteful.
	 * @param input the file to which is being processed
	 * @return the number of bytes in that file
	 * @throws Exception in case a fatal error occurs
	 */
	private int amountOfBytesInFile(FileInputStream input) throws Exception {
	    int numOfBytes = (int) input.getChannel().size();
        return numOfBytes;
	}
	/**
	 * This method gets the String fList and determined the amount of bytes that are in it. It 
	 * does this so it does have any issues when allocation takes place. Not enough space could cause issues and
	 * too much is wasteful.
	 * @param contents the strings that are in the List
	 * @return the total amount of bytes of the string
	 * @throws Exception in case of a fatal error
	 */
	private int amountOfBytesInArray(String [] contents) throws Exception {
	    int total = 0;
		for(int i = 0; i < contents.length; i++) {
	    	total += contents[i].length();
	    }
		return total;
	}
	/**
	 * This method gets the amount of bytes and takes its value into another byte array, which 
	 * will be the amount of bytes it really needs. each letter or number will be processed and passed through 
	 * the cipher method called update which will cipher the message. It will write it all on a newly created
	 * file.
	 * @param ci the cipher object that will actually encrypt the file
	 * @param out the file it will be written to
	 * @param amountofBytes the total amount of bytes in the list String
	 * @param contents the credentials in the string List
	 * @throws Exception in case of a fatal error.
	 */
	private void processFile(Cipher ci,OutputStream out,int amountofBytes,String [] contents) throws Exception{
		byte[] input = new byte[amountofBytes];
		for(int i = 0; i < contents.length; i++) {
			input = contents[i].getBytes();
		    byte[] output = ci.update(input);
		    if (output != null) 
		        out.write(output);
		    }
		    byte[] output = ci.doFinal();
		    if (output != null)
		    	out.write(output);
			out.flush();
			out.close();
	}	
	/**
	 * This method will take in the file of data the contains the encrypted credentials and will be processed into bytes of data
	 * first and then be processed as strings, which will then processed as a credential object whenever there is a multiple of four strings
	 * at a time. This means that there are only four fields created when encrypting the data(username,password, url, notes). It will get
	 * the amount of bytes from the file and will be used to store the bytes from the decrypted method. Each String is separated by a comma and will
	 * be split to separate it into one field. It will return an Observable list of objects so it can be processed into a table.
	 * @param ci cipher object that will decrypt the message
	 * @param in the file that will be read
	 * @param amountofBytes the total amount of bytes the file
	 * @return 
	 * @return return a Observable list of the credentials that were encrypted
	 * @throws Exception in case of a fatal error
	 */
	private void fileContents(Cipher ci,InputStream in,int amountofBytes) throws Exception {
		byte[] input = new byte[amountofBytes];
	    int read;
	    byte[] output = null;
	    int index = 0;
	    ArrayList<String> contents = new ArrayList<String>();
	    while ((read = in.read(input)) != -1 && index < 5) {
	         output = ci.update(input, 0, read);
	         index++;
	    }
	    
	    if(index == 5)
	    	System.exit(0);
	    	
	    String string = new String(output);
	    //finalize the remaining amount of bytes
	    output = ci.doFinal();
	    String string2 = new String(output);
	    string += string2;
	    in.close();	    
	    String [] values = string.split(",");
		for(int i =0; i <= values.length; i++) {
			if(i == values.length) {
				credentials.add(addCredentials(contents));
				contents.clear();
			}
			else if(i % 4 == 0 && i != 0) {
				credentials.add(addCredentials(contents));
				contents.clear();
				contents.add(values[i]);
			}
			else 
			contents.add(values[i]);
		}
		values = null;
		contents.clear();
		
	}
	/**
	 * This method gets each index of an arraylist and puts it into a Credentials class constructor
	 * to create a new object.
	 * @param contents contains an arraylist of string credentials. It's the contents od the array
	 * @return
	 */
	private Credentials addCredentials(ArrayList<String> contents) {
		return new Credentials(contents.get(0),contents.get(1),contents.get(2),contents.get(3));
	}
	
	public void clearCredentials() {
		credentials.clear();
	}
	public ObservableList<Credentials> getCredentials() {
		return credentials;
	}
	private void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}
	private void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		
	}
	public String getAbsolutePath() {
		return this.absolutePath;
	}
}
