package application.settings;


import application.MainMenuComponents;
import application.game.GamesModule;
import application.scoreboard.Scoreboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This class contains a single static method to create the settings layout of Quinzical
 * @author team 41
 */

public class Settings {

	/**
	 * This method creates the GUI layout to view settings
	 * @param window | main menu stage
	 * @param gameMenu | main menu scene
	 * @param score | scoreboard instance
	 * @param colourBlindButton | state of colour blind button 
	 */
	public static void viewSettings(Stage window, GamesModule gameMenu, Scoreboard score, Button colourBlindButton) {
		
		Scene menuScene = window.getScene();
		
		//Create settings title
		Text title = new Text("Settings");
		title.setFill(Color.ORANGE);
		title.setFont(Font.font("Times New Roman\"", FontWeight.BOLD, 36));
		title.setTextAlignment(TextAlignment.CENTER);
		title.setStyle("-fx-stroke: #2b1600;-fx-stroke-width: 1px;");
		title.setUnderline(true);
		
		//Create buttons for settings
		Button resetGameButton = SettingsComponents.getResetGameButton(gameMenu);
		
		Button resetScoreboardButton = SettingsComponents.getResetScoreboardButton(score);
		
		SettingsComponents.setupColourBlindButton(colourBlindButton);
		
		ComboBox<String> themeSelectBox = SettingsComponents.setBackgroundBox();
		
		Button changePlaybackSpeed = SettingsComponents.getChangePlaybackSpeedButton();
		
		Button back = SettingsComponents.getBackButton(window, menuScene);
		
		VBox coreButtonBox = new VBox();
		coreButtonBox.setSpacing(10);
		coreButtonBox.setAlignment(Pos.CENTER);
		coreButtonBox.getChildren().addAll(changePlaybackSpeed, colourBlindButton, themeSelectBox, resetGameButton, resetScoreboardButton);
		
		BorderPane settingBox = new BorderPane();
		settingBox.setPadding(new Insets(30));
		settingBox.setTop(title);
		settingBox.setCenter(coreButtonBox);
		settingBox.setBottom(back);
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(back, Pos.CENTER);
		settingBox.setBackground(MainMenuComponents.setBackground("rangitoto_sunset.png"));
		
		Scene settingScene = new Scene(settingBox, 800, 600);
		window.setScene(settingScene);
		window.show();
	}
}
