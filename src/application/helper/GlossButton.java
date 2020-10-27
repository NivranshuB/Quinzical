package application.helper;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * This class contains a single static method to add a gloss effect to a button
 * @author Nivranshu
 */

public class GlossButton {

	/**
	 *This method adds a gloss effect to the input button
	 * @param button | button to add gloss effect to
	 * @param font size | font size of the text
	 */
	public static Button addGlossEffect(Button button, int fontSize) {
		button.setStyle("-fx-background-color: #ECECEC, linear-gradient(#ECECEC 50%, #D5D5D5 100%), radial-gradient(center 50% -40%,"
				+ " radius 200%, #ECECEC 45%, #D5D5D5 50%);-fx-background-radius: 15;-fx-background-insets: 0,1,1;"
				+ "-fx-text-fill: black;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );-fx-font-size: " + fontSize);
		
		DropShadow borderGlow = new DropShadow();
		borderGlow.setColor(Color.RED);
		borderGlow.setOffsetX(0f);
		borderGlow.setOffsetY(0f);
		borderGlow.setHeight(80);
		borderGlow.setWidth(80);
		
		button.setEffect(borderGlow);
		
		return button;
	}
}
