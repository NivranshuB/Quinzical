package application.settings;

import application.game.GamesModule;
import application.helper.AlertBox;
import application.scoreboard.Scoreboard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsComponents {

	static Button getResetGameButton(GamesModule gameMenu) {
		
		Button resetGameButton = new Button("Click to reset current Games Modules game");
		resetGameButton.setPrefSize(400,60);
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
		colourBlindButton.setPrefSize(400, 60);
		colourBlindButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		colourBlindButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				if (colourBlindButton.getText().equals("Click to enable colour blind mode")) {
					colourBlindButton.setText("Click to disable colour blind mode");
				}
				else {
					colourBlindButton.setText("Click to enable colour blind mode");
				}
				
			}
		});
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
}