package quinzical;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main class for the Jeopardy application. This class launches the game window
 * handles all the actions to the main menu and the question board, and also fires
 * up any alert box, confirm box or text box requirements for the game.
 * 
 * @author Nivranshu Bose | 05/09/2020
 */
public class MainMenu extends Application {

	Stage _gameWindow;
	Scene _menuScene;//the default scene for the game (also the main menu)

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Implemented start method that initialises all components of the main
	 * menu scene including any buttons that the user interacts with.
	 */
	public void start(Stage PrimaryStage) {

		_gameWindow = PrimaryStage;

		PrimaryStage.setTitle("Quinzical: Main Menu");

		//Welcome message centered at the top of main menu
		StackPane menuText = new StackPane();
		Text welcome = new Text("Welcome to Jeopardy");
		welcome.setStyle("-fx-font-size: 30;");
		welcome.setTextAlignment(TextAlignment.CENTER);
		menuText.getChildren().add(welcome);
		StackPane.setAlignment(welcome, Pos.CENTER);

		//Prompt to the user to engage with the main menu options
		StackPane menuInfo = new StackPane();
		Text info = new Text("Please select one of the following options: ");
		info.setStyle("-fx-font-size: 12;");
		info.setTextAlignment(TextAlignment.CENTER);
		menuInfo.getChildren().add(info);
		StackPane.setAlignment(info, Pos.CENTER);

		//Prints out the question board in a new scene
		Button printBoardButton = new Button();
		printBoardButton.setText("Print question board");
		printBoardButton.setPrefSize(460,60);
		printBoardButton.setStyle("-fx-border-color: #200459;-fx-border-width: 1;-fx-font-size: 16;");
		printBoardButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				GamesModule gameMenu = new GamesModule();
				gameMenu.start(_gameWindow);
			}
		});

		//Prints out the question board in a new scene
		Button askQuestionButton = new Button();
		askQuestionButton.setText("Ask a question");
		askQuestionButton.setPrefSize(460,60);
		askQuestionButton.setStyle("-fx-border-color: #070459;-fx-border-width: 1;-fx-font-size: 16;");
		askQuestionButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				GamesModule gameMenu = new GamesModule();
				gameMenu.start(_gameWindow);
			}
		});

		//Prints out the player's winnings in a new window
		Button viewWinningsButton = new Button();
		viewWinningsButton.setText("View the current winnings");
		viewWinningsButton.setPrefSize(460,60);
		viewWinningsButton.setStyle("-fx-border-color: #0B478D;-fx-border-width: 1;-fx-font-size: 16;");
		viewWinningsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
			}
		});

		//Resets the game by reinitialising category and questions
		Button resetGameButton = new Button();
		resetGameButton.setText("Reset Game");
		resetGameButton.setPrefSize(460,60);
		resetGameButton.setStyle("-fx-border-color: #184FA0;-fx-border-width: 1;-fx-font-size: 16;");
		resetGameButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
			}
		});

		//Saves the category and questions data of the game into a file before exiting the game
		Button exitButton = new Button();
		exitButton.setText("Exit Game");
		exitButton.setPrefSize(460,60);
		exitButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				boolean confirmation = ConfirmBox.displayConfirm("Exit confirmation", "Are you sure "
						+ "you want to exit? (Don't worry your progress will be saved)");
				if (confirmation) {
					_gameWindow.close();
				}
			}
		});

		//Saves the category and questions data for the player into a file before exiting the game
		_gameWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle (WindowEvent e) {
				boolean confirmation = ConfirmBox.displayConfirm("Exit confirmation", "Are you sure "
						+ "you want to exit? (Don't worry your progress will be saved)");
				if (confirmation) {
					_gameWindow.close();
				}
				e.consume();
			}
		});

		//Set up the layout for the contents of the main menu
		VBox menuLayout = new VBox();
		menuLayout.setSpacing(10);
		menuLayout.setPadding(new Insets(20, 20, 30, 20)); 
		menuLayout.getChildren().addAll(menuText, menuInfo, printBoardButton, askQuestionButton,
				viewWinningsButton, resetGameButton, exitButton);

		_menuScene = new Scene(menuLayout, 500, 500);
		PrimaryStage.setScene(_menuScene);
		PrimaryStage.show();
	}
	
	/**
	 * 
	 */
	public Scene getScene() {
		return _menuScene;
	}

}
