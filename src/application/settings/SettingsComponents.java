package application.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import application.game.GamesModule;
import application.helper.AlertBox;
import application.questions.QuestionBox;
import application.scoreboard.Scoreboard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class SettingsComponents {
	
	private static String _save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "settings";
	private static String _saveColourBlind = "Click to enable colour blind mode";
	public static String _backgroundName = "rangitoto_sunset.png";

	static Button getResetGameButton(GamesModule gameMenu) {
		
		Button resetGameButton = new Button("Click to reset current Games Module game");
		resetGameButton.setPrefSize(400,60);
		resetGameButton.setAlignment(Pos.CENTER_LEFT);
		resetGameButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		resetGameButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				gameMenu.resetGame();
				AlertBox.displayAlert("Game reset", "Your current Games Module game has been reset", "#000000");
			}
		});
		
		return resetGameButton;
	}
	
	static Button getResetScoreboardButton(Scoreboard score) {
		Button resetScoreboardButton = new Button("Click to reset the scoreboard");
		resetScoreboardButton.setPrefSize(400,60);
		resetScoreboardButton.setAlignment(Pos.CENTER_LEFT);
		resetScoreboardButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		resetScoreboardButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				score.resetScoreboard();
				AlertBox.displayAlert("Scoreboard reset", "The scoreboard has been reset", "#000000");
			}
		});
		
		return resetScoreboardButton;
	}
	
	static void setupColourBlindButton(Button colourBlindButton) {
		colourBlindButton.setText(_saveColourBlind);
		colourBlindButton.setPrefSize(400, 60);
		colourBlindButton.setAlignment(Pos.CENTER_LEFT);
		colourBlindButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
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
	static ComboBox<String> setBackgroundBox() {
		
		ComboBox<String> backgroundBox = new ComboBox<String>();
		backgroundBox.setPrefSize(400, 60);
		backgroundBox.getItems().clear();
		backgroundBox.getItems().addAll("Rangitoto sunset", "Skytower night view");
		backgroundBox.setPromptText("Change Games Module background");
		backgroundBox.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
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
	static Button getChangePlaybackSpeedButton() {
		Button changePlaybackSpeedButton = new Button("Change speech playback speed");
		changePlaybackSpeedButton.setPrefSize(400, 60);
		changePlaybackSpeedButton.setAlignment(Pos.CENTER_LEFT);
		changePlaybackSpeedButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		changePlaybackSpeedButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {	
				QuestionBox.testPlaybackSpeed("Test", "This is a test to change the speech playback speed");
			}
			
		});
		
		return changePlaybackSpeedButton;
	}
	static Button getBackButton(Stage window, Scene menuScene) {
		Button back = new Button("Back");
		back.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				window.setScene(menuScene);
				window.show();
			}
		});
		
		return back;
	}
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
	public static String getSavedColourBlindMode() {
		return _saveColourBlind;
	}
}
