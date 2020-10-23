package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import application.game.GamesModule;
import application.game.Winnings;
import application.helper.AlertBox;
import application.helper.ConfirmBox;
import application.practice.PracticeModule;
import application.questions.QuestionBank;
import application.scoreboard.Scoreboard;
import application.settings.Settings;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private Winnings _currentWinnings;
	private int _winnings;
	private Stage _gameWindow;
	private Scene _menuScene;//the default scene for the game (also the main menu)
	private GamesModule _gameMenu;
	public static Button _colourBlindButton;
	public static Scoreboard _scoreboard;
	
	@Override
	public void start(Stage primaryStage) {
		_gameWindow = primaryStage;
		initialiseGameData();
		mainMenuInterface();
	}
	
	/**
	 * 
	 */
	private void initialiseGameData() {
		_questions = new QuestionBank();//initialise categories and questions
		_currentWinnings = new Winnings();//initialise winnings
		_winnings = _currentWinnings.getValue();
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
		
		Button checkPrize = MainMenuComponents.menuButton("Check your prize");
		checkPrize.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_currentWinnings = new Winnings();//reinitialise winnings
				_winnings = _currentWinnings.getValue();
				
				if (_winnings >= 4500) {
					//creating the image object
				    InputStream stream;
					try {
						String prize_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "winningsMeme.jpg";
						stream = new FileInputStream(prize_loc);
						Image image = new Image(stream);
					    //Creating the image view
					    ImageView imageView = new ImageView();
					    //Setting image to the image view
					    imageView.setImage(image);
					    //Setting the Scene object
					    Stage prizeStage = new Stage();
					    Group prizeGroup = new Group(imageView);
					    Scene prizeScene = new Scene(prizeGroup);
					    prizeStage.setResizable(false);
					    prizeStage.setTitle("Grand Prize");
					    prizeStage.setScene(prizeScene);
					    prizeStage.show();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				else {
					AlertBox.displayAlert("Prize error", "Please obtain the grand prize from Games Module to view your prize", 	"#000000");
				}
				
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
					_gameWindow.close();
				}
				e.consume();
			}
		});
		
		VBox menuLayout = MainMenuComponents.getMenuLayout();
		menuLayout.setBackground(MainMenuComponents.setBackground());
		menuLayout.getChildren().addAll(MainMenuComponents.getMenuTitleText(), MainMenuComponents.getMenuInfo(),
				gamesModuleButton, practiceModuleButton, viewScoreboard, checkPrize, settingsButton, exitButton);

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
