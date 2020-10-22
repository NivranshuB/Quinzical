package application.settings;

import application.game.GamesModule;
import application.scoreboard.Scoreboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Settings {

	/**
	 * 
	 */
	public static void viewSettings(Stage window, GamesModule gameMenu, Scoreboard score, Button colourBlindButton) {
		
		Scene menuScene = window.getScene();
		
		Text title = new Text("Settings");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		
		Button resetGameButton = SettingsComponents.getResetGameButton(gameMenu);
		
		Button resetScoreboardButton = SettingsComponents.getResetScoreboardButton(score);
		
		SettingsComponents.setupColourBlindButton(colourBlindButton);
		
		Button back = SettingsComponents.getBackButton(window, menuScene);
		
		VBox coreButtonBox = new VBox();
		coreButtonBox.setSpacing(10);
		coreButtonBox.setAlignment(Pos.CENTER);
		coreButtonBox.getChildren().addAll(resetGameButton, resetScoreboardButton, colourBlindButton);
		
		VBox settingBox = new VBox();
		settingBox.setSpacing(20);
		settingBox.setPadding(new Insets(20));
		settingBox.setAlignment(Pos.CENTER);
		settingBox.getChildren().addAll(title,coreButtonBox, back);
		
		Scene settingScene = new Scene(settingBox, 800, 600);
		window.setScene(settingScene);
		window.show();
	}
}
