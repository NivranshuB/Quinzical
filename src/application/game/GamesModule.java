package application.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import application.Main;
import application.MainMenuComponents;
import application.helper.AlertBox;
import application.helper.ConfirmBox;
import application.questions.Category;
import application.questions.Question;
import application.questions.QuestionBank;
import application.settings.SettingsComponents;
import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;
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
import javafx.stage.Stage;

/**
 * This class helps provide the functionality of the 'Games Module' for the Quinzical application.
 * The control of the application is passed to this class when the user presses the 'Games Module'
 * button in the main menu. The static method GamesModule.start(Stage primaryStage) is what launches
 * the Games Module menu, where the primaryStage is the main stage of the application. The 
 * GamesModule.start() returns control to the main menu either when the user presses the back button
 * or when there are no clues left to answer in the GamesModule and the user chooses not to press
 * 'Play Again' in the reward screen dialog. 
 * 
 * @author se2062020 - Team 41
 *
 */
public class GamesModule {

	QuestionBank _questionBank;//all access to category and questions is through this
	Winnings _currentWinnings;//allows the retrieval of saved winnings data
	int _winnings;//the total winnings for the player

	Stage _gameWindow;
	Scene _quesScene;//the games module scene for the application
	Scene _menuScene;//the main menu scene for the application

	boolean _returnToMenu;

	/**
	 * The main method passes control to the GamesModule by invoking this method. This method
	 * takes the main application's Stage as input and all of the GamesModule components are printed
	 * on a scene on this window.
	 */
	public void start(Stage primaryStage) {

		_menuScene = primaryStage.getScene();
		_currentWinnings = new Winnings();//initialise winnings
		_winnings = _currentWinnings.getValue();
		
		_gameWindow = primaryStage;
		_gameWindow.show();

		checkSaveData();
	}

	/**
	 * This method first checks if there exists a save file from which it should retrieve the saved
	 * categories and questions of a game that has not been finished yet. If such a file exists
	 * then it will set up the questionBank to consists of these saved questions, or else it will
	 * set up the _questionBank to consists of new categories and questions.
	 */
	private void checkSaveData() {

		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "save_data";
		File save_data = new File(save_loc);

		//check if there exists a save_data containing winnings and questions data
		if (Files.exists(save_data.toPath())) {
			String categoryDir = save_loc + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//load saved categories and questions data
			CategorySelectScene.moveInternationalToEnd(_questionBank);
			displayQuestionBoard();
		} else {
			String categoryDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//load new categories and questions data
			CategorySelectScene.categorySelector(_gameWindow, _questionBank, _menuScene, this);
		}
	}

	/**
	 * This method is responsible for creating and organising all the button, labels and scene
	 * elements of the Games Module scene. It distributes the responsibility of constructing
	 * the question/clue buttons of the game to the displayQuestions() method.
	 */
	void displayQuestionBoard() {

		//initial layout for the graphical layout of the category names
		HBox topMenu = GamesModuleComponents.getTopMenu();
		
		//initial setup for the graphical layout of the questions
		GridPane quesLayout = GamesModuleComponents.getQuesLayout();

		QuestionGrid.displayQuestions(topMenu, quesLayout, this, _questionBank);

		//Saves the category and questions data for the player into a file before exiting the game
		_gameWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle (WindowEvent e) {
				boolean confirmation = ConfirmBox.displayConfirm("Exit confirmation", "Are you sure "
						+ "you want to exit? (Don't worry your progress will be saved)");
				if (confirmation) {
					saveAndExitGame();
					_gameWindow.close();
				}
				e.consume();
			}
		});
		
		Button helpButton = GamesModuleComponents.getHelpButton(_gameWindow);
		
		//backButton will cause the control of the application to go back to the main menu
		Button backButton = GamesModuleComponents.getBackButton();
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				saveAndExitGame();
				_gameWindow.setScene(_menuScene);
				_gameWindow.show();
			}
		});

		//Label that will display the winnings value to the user in the Games Module menu
		Label winnings = GamesModuleComponents.getWinningsLabel("Winnings: $" + _winnings);

		StackPane backAllignment = new StackPane();
		backAllignment.getChildren().add(backButton);
		StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);

		//Bottom menu consists of the winnings amount and tbe back button
		HBox bottomMenu = new HBox();
		bottomMenu.setSpacing(180);
		bottomMenu.setAlignment(Pos.CENTER_LEFT);
		bottomMenu.getChildren().addAll(winnings, backAllignment, helpButton);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));

		//Overall layout for the Games Module scene/window
		BorderPane layout = new BorderPane();
		layout.setTop(topMenu);
		layout.setCenter(quesLayout);
		layout.setBottom(bottomMenu);
		layout.setBackground(MainMenuComponents.setBackground(SettingsComponents.getBackgroundName()));

		_quesScene = new Scene(layout, 800, 600);
		_gameWindow.setScene(_quesScene);
		_gameWindow.show();
	}

	/**
	 * This method deletes any previous save_data recursively and then rewrites 
	 * the question/category data and the winnings data into the save_data directory.
	 */
	private void saveAndExitGame() {

		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "save_data";
		Path pathCategoryData = Paths.get(save_loc + System.getProperty("file.separator") + "categories");
		
		try {
			Files.createDirectories(pathCategoryData);
		} catch (IOException e) {
			System.err.println("Failed to create directory!" + e.getMessage());
		}

		try {
			FileWriter winningsWriter = new FileWriter(save_loc + System.getProperty("file.separator") + "winnings");
			winningsWriter.write(String.valueOf((_winnings)));
			winningsWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Write data for each question to its respective category file
		for (Category c : _questionBank.getCategoryList()) {
			try {
				FileWriter categoryWriter = new FileWriter(save_loc + "/categories/" + c.getCategoryName());

				for (Question q : c.getQuestions()) {
					categoryWriter.write(q.getQuestion() + "|" + q.getValue() + "|" + q.getClue() + "|" + q.getFullAnswer() + "\n");
				}				
				categoryWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method deletes a specified directory and all its contents recursively.
	 * 
	 * @param directoryToBeDeleted 
	 * @return | true if File deleted successfully else return false
	 */
	private boolean deleteDirectory(File directoryToBeDeleted) {
		File[] contents = directoryToBeDeleted.listFiles();
		if (contents != null) {
			for (File file : contents) {
				deleteDirectory(file);
			}
		}

		return directoryToBeDeleted.delete();
	}

	/**
	 * This method deletes all the save_data for the game category/questions and the
	 * winnings and then reinitialises the category/questions and the winnings.
	 */
	public void resetGame() {

		//if saved data exists then also need to delete this
		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "save_data";
		File save_data = new File(save_loc);
		Main._scoreboard.setGameFinished(false);
		deleteDirectory(save_data);
	}

	/**
	 * This method displays a message with the total winnings earned by the user
	 * when the game is finished, i.e. when all the questions from all the categories
	 * have been attempted.
	 */
	void gameFinished() {
		
		_gameWindow.setScene(_menuScene);
		_gameWindow.show();

		boolean playAgain = ConfirmBox.displayConfirm("Play Again", "Would you like to play again?");

		if (playAgain) {
			resetGame();
			Main.restartGame(_gameWindow);
		} else {
			saveAndExitGame();
		}
	}
	
	int getWinnings() {
		return _winnings;
	}
	
	void setWinnings(int newWinnings) {
		_winnings = newWinnings;
	}
	
}
