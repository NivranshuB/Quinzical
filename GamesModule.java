package jeopardy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GamesModule extends Application{

	QuestionBank _questionBank;//all access to category and questions is through this
	Winnings _currentWinnings;//allows the retrieval of saved winnings data
	Stage _gameWindow;
	Scene _menuScene;//the default scene for the game (also the main menu)
	int _winnings;//the total winnings for the player

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {

		_gameWindow = primaryStage;
		checkSaveData();
		
		for (Category c : _questionBank.getCategoryList()) {
			for (Question q : c.getQuestions()) {
				System.out.println(q);
			}
		}
	}

	private void checkSaveData() {

		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "save_data";
		File save_data = new File(save_loc);

		if (Files.exists(save_data.toPath())) {
			String categoryDir = save_loc + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//load saved categories and questions data
		} else {
			String categoryDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//initialise new categories and questions
			initialiseNewQuestions();
		}
	}

	private void initialiseNewQuestions() {

	}

	
}
