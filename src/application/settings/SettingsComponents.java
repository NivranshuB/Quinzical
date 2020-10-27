package application.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import application.game.GamesModule;
import application.helper.ConfirmBox;
import application.helper.GlossButton;
import application.questions.QuestionBox;
import application.scoreboard.Scoreboard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;


/**
 * This class contains methods to create and constrain GUI components for settings
 * @author Whan Jung
 */
public class SettingsComponents {
	
	private static String _save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "settings";
	private static String _saveColourBlind = "Click to enable colour blind mode";
	public static String _backgroundName = "rangitoto_sunset.png";

	/**
	 * This method creates and sets constraints for the reset game button
	 * @return reset game button
	 */
	static Button getResetGameButton(GamesModule gameMenu) {
		
		Button resetGameButton = new Button("Click to reset current Games Module game");
		resetGameButton.setPrefSize(440,60);
		resetGameButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		resetGameButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				boolean resetGame = ConfirmBox.displayConfirm("Game reset", "Are you sure you want to reset the game?"
						+ " (This will remove all the current questions in the Games Module)");
				if (resetGame) {
					gameMenu.resetGame();
				}
			}
		});
		
		return resetGameButton;
	}
	/**
	 * This method creates and sets constraints for the reset scoreboard button
	 * @return reset scoreboard button
	 */
	static Button getResetScoreboardButton(Scoreboard score) {
		Button resetScoreboardButton = new Button("Click to reset the scoreboard");
		resetScoreboardButton.setPrefSize(440,60);
		resetScoreboardButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		resetScoreboardButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				boolean resetScoreboard = ConfirmBox.displayConfirm("Game reset", "Are you sure you want to reset"
						+ " the Scoreboard? (This will remove all scores in the Scoreboard)");
				if (resetScoreboard) {
					score.resetScoreboard();
				}
			}
		});
		
		return resetScoreboardButton;
	}
	/**
	 * This method sets the constraints for the set up of colourblind button
	 * 
	 */
	static void setupColourBlindButton(Button colourBlindButton) {
		colourBlindButton.setText(_saveColourBlind);
		colourBlindButton.setPrefSize(440, 60);
		colourBlindButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		//Update button label and function everytime button is clicked
		colourBlindButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				if (colourBlindButton.getText().equals("Click to enable colour blind mode")) {
					colourBlindButton.setText("Click to disable colour blind mode");
					_saveColourBlind = "Click to disable colour blind mode";
				}
				else {
					colourBlindButton.setText("Click to enable colour blind mode");
					_saveColourBlind = "Click to enable colour blind mode";
				}
				
			}
		});
	}
	/**
	 * This method creates and sets constraints for the combobox to change background of games module
	 *
	 */
	static ComboBox<String> setBackgroundBox() {
		
		ComboBox<String> backgroundBox = new ComboBox<String>();
		backgroundBox.setPrefSize(440, 60);
		backgroundBox.getItems().clear();
		backgroundBox.getItems().addAll("Rangitoto sunset", "Skytower night view");
		backgroundBox.setPromptText("Change Games Module background");
		backgroundBox.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backgroundBox.setPadding(new Insets(0, 0, 0, 45));
		backgroundBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (backgroundBox.getValue().equals("Rangitoto sunset")) {
					_backgroundName = "rangitoto_sunset.png";
				} else if (backgroundBox.getValue().equals("Skytower night view")) {
					_backgroundName = "skytower_night_view.png";
				}
				
			}
			
		});
		return backgroundBox;
	}
	/**
	 *  This method creates and sets constraints for the change playback speed button
	 * @return change playback speed button
	 */
	static Button getChangePlaybackSpeedButton() {
		Button changePlaybackSpeedButton = new Button("Change speech playback speed");
		changePlaybackSpeedButton.setPrefSize(440, 60);
		changePlaybackSpeedButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		changePlaybackSpeedButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {	
				QuestionBox.testPlaybackSpeed("Test", "This is a test to change the speech playback speed");
			}
			
		});
		
		return changePlaybackSpeedButton;
	}
	/**
	 * This method creates and sets constraints for the back button
	 * @param window | main menu stage
	 * @param menuScene | main menu Scene
	 */
	static Button getBackButton(Stage window, Scene menuScene) {
		Button back = new Button("Back");
		back.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				window.setScene(menuScene);
				window.show();
			}
		});
		back = GlossButton.addGlossEffect(back, 18);
		return back;
	}
	/**
	 * This method saves the current settings data to a file in directory
	 *
	 */
	public static void saveSettingData() {
		
		//Write colour blind mode and background setting to file
		try {
			FileWriter writer = new FileWriter(_save_loc);
			writer.write(_saveColourBlind + "\n");
			writer.write(_backgroundName);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method reads and sets saved settings everytime Quinzical is booted up
	 *
	 */
	public static void setSettingsFromFile() {

		File settingsFile = new File(_save_loc);
		//If file exists then read it
		if (settingsFile.exists()) {
			try {
				Scanner scanner = new Scanner(new FileReader(settingsFile));
				
				//Gets the colour blind and background settings
				_saveColourBlind = scanner.nextLine();
				_backgroundName = scanner.nextLine();
			
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String getBackgroundName() {
		return _backgroundName;
	}
	/**
	 * This method returns the current colour blind mode state
	 * @return saved colour blind mode
	 */
	public static String getSavedColourBlindMode() {
		return _saveColourBlind;
	}
}
