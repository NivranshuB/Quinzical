package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import games.GamesModule;
import games.Winnings;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import practice.PracticeModule;
import questions.QuestionBank;
import quinzical.AlertBox;
import quinzical.ConfirmBox;



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
	
	@Override
	public void start(Stage primaryStage) {
		
		_questions = new QuestionBank();//initialise categories and questions
		_currentWinnings = new Winnings();//initialise winnings
		_winnings = _currentWinnings.getValue();
		_gameMenu = new GamesModule();
		_colourBlindButton = new Button("Click to enable colour blind mode");
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
				_gameMenu.start(_gameWindow);
			}
		});
		
		Button practiceModuleButton = new Button("Practice Module");
		practiceModuleButton.setPrefSize(460,60);
		practiceModuleButton.setStyle("-fx-border-color: #070459;-fx-border-width: 1;-fx-font-size: 16;");
		
		Button checkPrize = new Button("Check your prize");
		checkPrize.setPrefSize(460,60);
		checkPrize.setStyle("-fx-border-color: #070459;-fx-border-width: 1;-fx-font-size: 16;");
		checkPrize.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_currentWinnings = new Winnings();//reinitialise winnings
				_winnings = _currentWinnings.getValue();
				
				if (_winnings >= 4500) {
					//creating the image object
				    InputStream stream;
					try {
						stream = new FileInputStream("winningsMeme.jpg");
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
		
		Button settingsButton = new Button("Settings");
		settingsButton.setPrefSize(460,60);
		settingsButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				settings(primaryStage);
			}
		});
		
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
		menuLayout.getChildren().addAll(menuTitleText, menuInfo, gamesModuleButton, practiceModuleButton, checkPrize, settingsButton, exitButton);

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
	 * Settings scene that allows the user to change to color blindness mode, or lets the user reset the game.
	 * @param primaryStage
	 */
	public void settings(Stage primaryStage) {
		
		Text title = new Text("Settings");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		
		Button resetGameButton = new Button("Click to reset current Games Modules game");
		resetGameButton.setPrefSize(400,60);
		resetGameButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		resetGameButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_gameMenu.resetGame();
				AlertBox.displayAlert("Game reset", "Your current Games Module game has been reset", "#000000");
			}
		});
		
		_colourBlindButton.setPrefSize(400, 60);
		_colourBlindButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		_colourBlindButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				if (_colourBlindButton.getText().equals("Click to enable colour blind mode")) {
					_colourBlindButton.setText("Click to disable colour blind mode");
				}
				else {
					_colourBlindButton.setText("Click to enable colour blind mode");
				}
				
			}
		});
		
		Button back = new Button("Back");
		back.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				primaryStage.setScene(_menuScene);
				primaryStage.show();
			}
		});
		
		VBox coreButtonBox = new VBox();
		coreButtonBox.setSpacing(10);
		coreButtonBox.getChildren().addAll(resetGameButton, _colourBlindButton);
		
		VBox settingBox = new VBox();
		settingBox.setSpacing(20);
		settingBox.setPadding(new Insets(20));
		settingBox.setAlignment(Pos.CENTER);
		settingBox.getChildren().addAll(title,coreButtonBox, back);
		Scene settingScene = new Scene(settingBox);
		primaryStage.setScene(settingScene);
		primaryStage.show();
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
