package main;

import games.GamesModule;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import practice.PracticeModule;
import questions.QuestionRetriever;
import quinzical.ConfirmBox;



/**
 * GUI for main menu
 * @author Whan Jung
 *
 */
public class Main extends Application {
	
	QuestionRetriever _questions;//all access to category and questions is through this
	Stage _gameWindow;
	Scene _menuScene;//the default scene for the game (also the main menu)
	
	@Override
	public void start(Stage primaryStage) {
		
		_questions = new QuestionRetriever();//initialise categories and questions
		_gameWindow = primaryStage;
		primaryStage.setTitle("Quinzical!");

		StackPane menuTitleText = new StackPane();
		Text welcomeText = new Text("Welcome to Quinzical");
		welcomeText.setFill(Color.ORANGE);
		welcomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		menuTitleText.getChildren().add(welcomeText);
		StackPane.setAlignment(welcomeText, Pos.CENTER);
		
		StackPane menuInfo = new StackPane();
		Text infoText = new Text("Please select one of the following options: ");
		infoText.setStyle("-fx-font-size: 15;");
		infoText.setTextAlignment(TextAlignment.CENTER);
		menuInfo.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		Button gamesModuleButton = new Button("Games Module");
		gamesModuleButton.setPrefSize(460,60);
		gamesModuleButton.setStyle("-fx-border-color: #200459;-fx-border-width: 1;-fx-font-size: 16;");
		gamesModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				GamesModule gameMenu = new GamesModule();
				gameMenu.start(_gameWindow);
			}
		});
		
		Button practiceModuleButton = new Button("Practice Module");
		practiceModuleButton.setPrefSize(460,60);
		practiceModuleButton.setStyle("-fx-border-color: #070459;-fx-border-width: 1;-fx-font-size: 16;");
		
		Button exitButton = new Button("Exit Game");
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
		
		VBox menuLayout = new VBox();
		menuLayout.setSpacing(10);
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.setPadding(new Insets(20, 20, 30, 20)); 
		menuLayout.getChildren().addAll(menuTitleText, menuInfo, gamesModuleButton, practiceModuleButton, exitButton);

		_menuScene = new Scene(menuLayout, 700, 450);
		practiceModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				PracticeModule module = new PracticeModule(_gameWindow, _questions, _menuScene);
				module.practiceModule();
			}
		});
		primaryStage.setScene(_menuScene);
		primaryStage.show();
		
		
	}
	
	/**
	 * 
	 */
	public static void restartGame(Stage gameWindow) {
		GamesModule newGame = new GamesModule();
		newGame.start(gameWindow);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);		
	}
}
