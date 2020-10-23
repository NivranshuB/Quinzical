package application;

import application.game.GamesModule;
import application.game.Winnings;
import application.helper.ConfirmBox;
import application.practice.PracticeModule;
import application.questions.QuestionBank;
import application.scoreboard.Scoreboard;
import application.settings.Settings;
import application.settings.SettingsComponents;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * GUI for main menu. This class launches the application and contains all the buttons to
 * navigate between different scenes of the application.
 * @author Whan Jung
 *
 */
public class Main extends Application {
	
	private QuestionBank _questions;//all access to category and questions is through this
	public static Stage _gameWindow;
	private Scene _menuScene;//the default scene for the game (also the main menu)
	private GamesModule _gameMenu;
	public static Button _colourBlindButton;
	public static Scoreboard _scoreboard;
	
	@Override
	public void start(Stage primaryStage) {
		_gameWindow = primaryStage;
		initialiseGameData();
		SettingsComponents.setSettingsFromFile();
		_colourBlindButton.setText(SettingsComponents.getSavedColourBlindMode());
		mainMenuInterface();
	}
	
	/**
	 * 
	 */
	private void initialiseGameData() {
		_questions = new QuestionBank();//initialise categories and questions
		_gameMenu = new GamesModule();
		_scoreboard = new Scoreboard();
		_colourBlindButton = new Button("Click to enable colour blind mode");
	}
	
	/**
	 * 
	 */
	private void mainMenuInterface() {
		
		_gameWindow.setTitle("Quinzical!");

		Button gamesModuleButton = MainMenuComponents.menuButton("Games Module");
		gamesModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_gameMenu.start(_gameWindow);
			}
		});
		
		Button practiceModuleButton = MainMenuComponents.menuButton("Practice Module");
		practiceModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				PracticeModule module = new PracticeModule(_gameWindow, _questions, _menuScene);
				module.practiceModule();
			}
		});
		
		Button viewScoreboard = MainMenuComponents.menuButton("View Scoreboard");
		viewScoreboard.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_scoreboard.ViewScoreboard(_gameWindow, _menuScene);
			}
		});
		
		Button settingsButton = MainMenuComponents.menuButton("Settings");
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				Settings.viewSettings(_gameWindow, _gameMenu, _scoreboard, _colourBlindButton);
			}
		});
		
		Button exitButton = MainMenuComponents.menuButton("Exit");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				boolean confirmation = ConfirmBox.displayConfirm("Exit confirmation", "Are you sure "
						+ "you want to exit? (Don't worry your progress will be saved)");
				if (confirmation) {
					SettingsComponents.saveSettingData();
					_scoreboard.saveScoresToFile();
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
					SettingsComponents.saveSettingData();
					_scoreboard.saveScoresToFile();
					_gameWindow.close();
				}
				e.consume();
			}
		});

		VBox menuLayout = MainMenuComponents.getMenuLayout();
		menuLayout.setBackground(MainMenuComponents.setBackground("rangitoto_sunset.png"));
		menuLayout.getChildren().addAll(MainMenuComponents.getMenuTitleText(), MainMenuComponents.getMenuInfo(),
				gamesModuleButton, practiceModuleButton, viewScoreboard, settingsButton, exitButton);

		_menuScene = new Scene(menuLayout, 800, 600);

		_gameWindow.setScene(_menuScene);
		_gameWindow.show();
	}
	
	/**This method launches a new Games Module.
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
	
	/**
	 * Checks if the color blind mode is enabled or not. If enabled returns true, else returns false.
	 * @return
	 */
	public static boolean colourBlindMode() {
		if (_colourBlindButton.getText().equals("Click to enable colour blind mode")) {
			return false;
		}
		else {
			return true;
		}
	}
}
