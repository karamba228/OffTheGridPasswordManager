package application.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.paint.Color;

public class Preferences {
	
	/**
	 * This method will take in the object settings and write the object into a file. When the program loads up, the
	 * file will be read and load up the data in that object.
	 * @param settings
	 * @throws IOException
	 */
	public static void setPreferences(Settings settings) throws IOException {
		FileOutputStream file = new FileOutputStream("src/application/Resources/preferences.ser");
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.writeObject(settings);
		out.close();
		file.close();
	}
	
	/**
	 * This method will read the file that was created when the preferences were decided and will load them in to the project.
	 * If there is not file found it will restore to the default values.
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Settings loadPreferences() throws IOException, ClassNotFoundException{
		Settings settings = null;
		FileInputStream file;
		try {
			file = new FileInputStream("src/application/Resources/preferences.ser");
			ObjectInputStream in = new ObjectInputStream(file);
			settings = (Settings) in.readObject();
			in.close();
			file.close();
			return settings;
		} catch (FileNotFoundException e) {
			settings = new Settings(Color.TOMATO.toString(),Color.WHEAT.toString(),Color.CORNFLOWERBLUE.toString()
					,Color.SEAGREEN.toString(),Color.LIGHTGOLDENRODYELLOW.toString(),Color.SEAGREEN.toString(),false);
		}
		return settings;
	}

}
