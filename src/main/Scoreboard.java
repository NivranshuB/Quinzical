package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;



/**
 * This class stores final winnings of the user and contains a function to view the scoreboard from the main menu
 * @author Whan Jung
 *
 */
public class Scoreboard {
	
	//Initialise array for scores
	private static ArrayList<String[]> _scoreList = new ArrayList<String[]>();
	private String _save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "scoreboard";
	private int _gameFinished;
	
	/**
	 * Constructor that searches for a scoreboard file and transfers scores into _scoreList
	 *
	 */
	public Scoreboard() {
		String[] splitLine;

		File scoreFile = new File(_save_loc);
		//If file exists then read it
		if (scoreFile.exists()) {
			try {
				Scanner scanner = new Scanner(new FileReader(scoreFile));
				
				//Gets the game finished boolean
				_gameFinished = Integer.parseInt(scanner.nextLine());
				
				//Add each score to array
				while (scanner.hasNextLine()) {
					splitLine = scanner.nextLine().split(",");
					_scoreList.add(splitLine);
						
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Changes scene from main menu to the viewing of the scoreboard
	 *@param mainStage | main menu stage
	 *@param mainMenuScene | main menu scene
	 */
	public void ViewScoreboard(Stage mainStage, Scene mainMenuScene) {
		
		Font headingStyle = Font.font("Verdana", FontWeight.BOLD, 25);
		Font scoreStyle = Font.font("Verdana", 20);
		
		BorderPane screenPane = new BorderPane();
		screenPane.setPadding(new Insets(20));
		
		//Create scoreboard title
		Text title = new Text("Scoreboard");
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
		title.setUnderline(true);
		
		//Create GridPane and Text for scores and headings
		GridPane scoreBoardPane = new GridPane();
		GridPane headingPane = new GridPane();
		headingPane.setHgap(200);
		scoreBoardPane.setAlignment(Pos.CENTER);
		scoreBoardPane.setVgap(10);
		scoreBoardPane.setHgap(250);
		scoreBoardPane.setAlignment(Pos.TOP_CENTER);
		Text rankText = new Text("Rank");
		Text nameText =	new Text("Name");
		Text scoreText =new Text("Score");
		rankText.setFont(headingStyle);
		nameText.setFont(headingStyle);
		scoreText.setFont(headingStyle);
		headingPane.add(rankText, 0, 0);
		headingPane.add(nameText, 1, 0);
		headingPane.add(scoreText, 2, 0);
		
		int rank = 1;
		
		//Sort scores before listing them on scoreboard
		sortScorelist();
		//Add scores in descending order to scoreboard
		for (String[] score: _scoreList) {
			rankText = new Text(String.valueOf(rank));
			nameText = new Text(score[0]);
			scoreText = new Text(score[1]);
			rankText.setFont(scoreStyle);
			nameText.setFont(scoreStyle);
			scoreText.setFont(scoreStyle);
			scoreBoardPane.add(rankText, 0, rank - 1);
			scoreBoardPane.add(nameText, 1, rank - 1);
			scoreBoardPane.add(scoreText, 2, rank - 1);
			rank++;
		}

		//Create title and headings
		VBox titleAndHeading = new VBox();
		titleAndHeading.setSpacing(10);
		titleAndHeading.setAlignment(Pos.CENTER);
		titleAndHeading.getChildren().addAll(title, headingPane);
		ScrollPane scrollPane = new ScrollPane(scoreBoardPane);
		scrollPane.setPadding(new Insets(5, 20, 5, 20));
		
		//Back button to main menu
		Button back = new Button("back");
		back.setFont(scoreStyle);
		BorderPane.setAlignment(back, Pos.CENTER);
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				mainStage.setScene(mainMenuScene);
				mainStage.show();
			}
		});

		screenPane.setTop(titleAndHeading);
		screenPane.setCenter(scrollPane);
		screenPane.setBottom(back);
		Scene scene = new Scene(screenPane, 800, 600);
		mainStage.setScene(scene);
		mainStage.show();
		
	}
	/**
	 * Once a game finishes, if user saves their name then add name and score into scoreboard
	 *@param title | title of popup
	 *@param message | message of popup
	 *@param colour | colour of message
	 *@param winning | final winnings of user
	 */
	public void addToScoreboard(String title, String message, String color, int winning) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(250);
		
		Label label = new Label(message);
		label.setWrapText(true);
		label.setPadding(new Insets(20, 20, 20, 20));
		label.setStyle("-fx-font-size: 18;-fx-text-fill: " + color);
		
		Text askName = new Text("Save your name to scoreboards");
		askName.setFont(Font.font("Verdana", 14));
		TextField namePrompt = new TextField();
		
		//Saves the users score and exits
		namePrompt.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
					String[] score = new String[2];
					score[0] = namePrompt.getText();
					score[1] = Integer.toString(winning);
					_scoreList.add(score);
					_gameFinished = 1;
					window.close();
				}
			}
		});
		
		//Saves the users score and exits
		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String[] score = new String[2];
				score[0] = namePrompt.getText();
				score[1] = Integer.toString(winning);
				_scoreList.add(score);
				_gameFinished = 1;
				window.close();
			}
		});
		
		//Doesn't save the user score and exits
		Button dontSave = new Button("Don't Save");
		dontSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				_gameFinished = 1;
				window.close();
			}
		});
		
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(save,dontSave);
		
		//VBox to add all components for adding scores scene
		VBox layout = new VBox();
		layout.setSpacing(10);
		layout.setPadding(new Insets(0, 40, 20, 40));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, askName, namePrompt, buttons);
		
		Scene scene = new Scene(layout, 500, 250);
		window.setScene(scene);
		window.showAndWait();
		
	}
	/**
	 *Sorts the scores in descending order
	 *
	 */
	private void sortScorelist() {
		//Uses Comparator to sort list in ascending order
		Collections.sort(_scoreList, new Comparator<String[]>() {

			@Override
			public int compare(String[] score1, String[] score2) {
				if (Integer.parseInt(score1[1]) <= Integer.parseInt(score2[1])) {
					return -1;
				} else if (Integer.parseInt(score1[1]) > Integer.parseInt(score2[1])) {
					return 1;
				} else {
					return 0;
				}
			}
			
		});
		//Reverses the array so it is in descending order
		Collections.reverse(_scoreList);
	}
	/**
	 * Save all scores to scoreboard file
	 *
	 */
	public void saveScoresToFile() {
		String scoreString = "";
		
		//If score list is not empty
		if (!_scoreList.isEmpty()) {
			try {
				FileWriter writer = new FileWriter(_save_loc);
				//write the game finished boolean to the file
				writer.write(_gameFinished + "\n");
				
				//Write the scores to the file
				for (String[] score: _scoreList) {
					scoreString = score[0] + "," + score[1] + "\n";
					writer.write(scoreString);
				}		
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Resets scoreboard and deletes are stored data
	 *
	 */
	public void resetScoreboard() {
		_gameFinished = -1;
		File file = new File(_save_loc);
		_scoreList.clear();
		file.delete();
		
	}
	/**
	 *boolean to determine if game finished but not reset to remove popup of saving scores from same user multiple times
	 *
	 */
	public boolean gameFinished() {
		if (_gameFinished == 1) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 *Sets the value of _gameFinished
	 *@param finished | finished boolean
	 *
	 */
	public void setGameFinished(boolean finished) {
		if (finished) {
			_gameFinished = 1;
		} else {
			_gameFinished = -1;
		}
	}
}
