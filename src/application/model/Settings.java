package application.model;
import java.io.Serializable;

public class Settings implements Serializable{
	private String colorLocate;
	private String colorSettings;
	private String colorNewFile;
	private String colorMainMenuForSettings;
	private String colorMainMenuForNewFile;
	private String settingsColorDisableButton;
	private boolean disableAnimation;
	
	public Settings(String colorLocate, String colorSettings, String colorNewFile, String colorMainMenuForSettings, String colorMainMenuForNewFile,String settingsColorDisableButton, boolean disableAnimation) {
		this.colorLocate = colorLocate;
		this.colorSettings = colorSettings;
		this.colorNewFile = colorNewFile;
		this.colorMainMenuForSettings = colorMainMenuForSettings;
		this.colorMainMenuForNewFile = colorMainMenuForNewFile;
		this.settingsColorDisableButton = settingsColorDisableButton;
		this.disableAnimation = disableAnimation;
	}
	
	
	public String getColorLocate() {
		return colorLocate;
	}

	public void setColorLocate(String colorLocate) {
		this.colorLocate = colorLocate;
	}

	public String getColorSettings() {
		return colorSettings;
	}

	public void setColorSettings(String colorSettings) {
		this.colorSettings = colorSettings;
	}

	public String getColorNewFile() {
		return colorNewFile;
	}

	public void setColorNewFile(String colorNewFile) {
		this.colorNewFile = colorNewFile;
	}

	public String getColorMainMenuForSettings() {
		return colorMainMenuForSettings;
	}

	public void setColorMainMenuForSettings(String colorMainMenuForSettings) {
		this.colorMainMenuForSettings = colorMainMenuForSettings;
	}

	public String getColorMainMenuForNewFile() {
		return colorMainMenuForNewFile;
	}

	public void setColorMainMenuForNewFile(String colorMainMenuForNewFile) {
		this.colorMainMenuForNewFile = colorMainMenuForNewFile;
	}

	public boolean isDisableAnimation() {
		return disableAnimation;
	}

	public void setDisableAnimation(boolean disableAnimation) {
		this.disableAnimation = disableAnimation;
	}
	
	public String getsettingsColorDisableButton() {
		return settingsColorDisableButton;
	}

	public void setsettingsColorDisableButton(String settingsColorDisableButton) {
		this.settingsColorDisableButton = settingsColorDisableButton;
	}


}
