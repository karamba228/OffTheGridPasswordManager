package application.view;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.RotateTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Animations {
	
	private boolean disableAnimations;
	//private static int counter
	
	/**
	 * This method will take in all the determined parameters and play the rotated animation 
	 * accordingly.
	 * @param node This will be any node that wants to be rotated. Our usage is the circles
	 * @param reverse to see if the animation wants to be played in reverse
	 * @param angle to see if the animation will rotate at a certain angle
	 * @param time the amount of time each rotation will take
	 * @param rate how fast the rotations will take to plav.
	 * @param cycle how many times the animation will be played
	 * 
	 * 
	 */
	public void playRotationAnimation(Node node, Boolean reverse,int angle,double d,int rate, int cycle) {
		if(disableAnimations == true)
			return;
		RotateTransition rotation = new RotateTransition(Duration.seconds(d),node);
		rotation.setAutoReverse(reverse);
		rotation.setByAngle(angle);
		rotation.setDelay(Duration.seconds(0));
		rotation.setRate(rate);
		rotation.setCycleCount(cycle);
		rotation.play();
	}
	/**
	 * This method will take in the preferred circle and color, which will then be
	 * processed to be filled on call. The cycle will on be one time since the desired color
	 * will be filled. This animation also syncs up with the 3 smaller circle rotations.
	 * @param c the determined circle that wants the strokes to be filled
	 * @param color the color of preference the user want to use
	 */
	public void fillAnimation(Circle c, Color color) {
		if(disableAnimations == true)
			return;
		StrokeTransition stroke = new StrokeTransition(); 
	    stroke.setAutoReverse(false);   
	    stroke.setCycleCount(1);  
	    stroke.setDuration(Duration.millis(300));       
	    stroke.setToValue(color);    
	    stroke.setShape(c);  
	    stroke.play();
	}
	/**
	 * This method will fill the rectangle whenever the mouse hovers over the button
	 * @param r the rectangle shaped that will be animated
	 * @param color the desired color to be filled with in the animation
	 */
	public void fillRectangle(Rectangle r, Color color) {
		if(disableAnimations == true)
			return;
	    FillTransition fill = new FillTransition();
	    fill.setAutoReverse(true);
	    fill.setCycleCount(1);
	    fill.setDuration(Duration.millis(200));
	    fill.setToValue(color);
	    fill.setShape(r);
	    fill.play();
	}
	
	/**
	 * This method will be invoked whenever the file has been successfully decrypted.
	 * It will be played for 1000 of a mill second.
	 * @param node the node that will be fade out to 
	 * @return the fade transition object to the maincontroller to determine if the animation is done playing.
	 */
	public FadeTransition fadeOut(Node node){
		FadeTransition ft = new FadeTransition(Duration.millis(3000), node);
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.play();
		return ft;
	}
	
	public void setAnimationStatus(boolean disableAnimations) {
		this.disableAnimations = disableAnimations;
	}
	
	public boolean gettAnimationStatus() {
		return this.disableAnimations;
	}
	public void countDown() {
		System.out.println("Hello");
	
	}
}
