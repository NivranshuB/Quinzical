package games;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;
import main.Main;
import questions.Category;
import questions.Question;
import questions.QuestionBank;
import questions.QuestionBox;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quinzical.AlertBox;
import quinzical.ConfirmBox;

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

	boolean _questionsLeft = false; 

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

		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "save_data";
		File save_data = new File(save_loc);

		//check if there exists a save_data containing winnings and questions data
		if (Files.exists(save_data.toPath())) {
			String categoryDir = save_loc + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//load saved categories and questions data
			moveInternationalToEnd();
			displayQuestionBoard();
		} else {
			String categoryDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "categories";
			_questionBank = new QuestionBank(categoryDir);//load new categories and questions data
			categorySelector();
		}
	}

	/**
	 * Since at the start of the Games Module, if there is no saved question data the game must
	 * randomly select 5 categories and 5 clues/questions, this method helps remove the extra
	 * categories in excess of 5, and then the extra questions of these categories in excess of 5
	 * randomly from the _questonBank.
	 */
	private void initialiseNewQuestions() {

		int numberOfCategories = _questionBank.getCategoryList().size();

		//until the number of categories in the question bank is not equal to 5, randomly remove a category
		while (numberOfCategories > 5) {
			int randomCategory = generateRandomNumber(0, numberOfCategories);
			_questionBank.getCategoryList().remove(randomCategory);
			numberOfCategories = _questionBank.getCategoryList().size();
		}

		//until the number of questions in a particular category is not equal to 5, randomly remove a question
		for (Category c : _questionBank.getCategoryList()) {
			int numberOfQuestions = c.getQuestions().size();
			while (numberOfQuestions > 5) {
				int randomQuestion = generateRandomNumber(0, numberOfQuestions);
				c.getQuestions().remove(randomQuestion);
				numberOfQuestions = c.getQuestions().size();
			}
		}

		//Assign the value of the questions for each category randomly from 100, 200, 300, 400, 500.
		int value;
		for (Category c : _questionBank.getCategoryList()) {
			value = 100;
			for (Question q : c.getQuestions()) {
				q.setValue(value);
				value += 100;
			}
		}
	}

	/**
	 * Static method that given the lower limit and the upper limit, randomly returns an int
	 * value n such that, low <= n < high.
	 * @param low: lower limit
	 * @param high: upper limit
	 * 
	 * @return random int value within range
	 */
	public static int generateRandomNumber(int low, int high) {
		Random random = new Random();
		return random.nextInt(high-low) + low;
	}

	/**
	 * This method is responsible for creating and organising all the button, labels and scene
	 * elements of the Games Module scene. It distributes the responsibility of constructing
	 * the question/clue buttons of the game to the displayQuestions() method.
	 */
	private void displayQuestionBoard() {

		//initial layout for the graphical layout of the category names
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(5, 5, 5, 5));
		topMenu.setSpacing(8);

		//initial setup for the graphical layout of the questions
		GridPane quesLayout = new GridPane();
		quesLayout.setPadding(new Insets(20, 20, 20, 20));
		quesLayout.setVgap(20);
		quesLayout.setHgap(50);

		displayQuestions(topMenu, quesLayout);

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
		
		double r = 20;
		Button helpButton = new Button("?");
		helpButton.setShape(new Circle(r));
		helpButton.setMinSize(2*r, 2*r);
		helpButton.setMaxSize(2*r, 2*r);
		helpButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		helpButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: #F0F8FF");
		
		Text helpText = new Text("Help #1: You can only attempt the lowest value question of each category"
				+ "\n\nHelp #2: International category will be unlocked after you have attempted at least two categories");
	    helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
	    
		Stage helpButtonStage = new Stage();
		helpButtonStage.initOwner(_gameWindow);
		helpButtonStage.initStyle(StageStyle.TRANSPARENT);
	    StackPane helpButtonPane = new StackPane();
	    helpButtonPane.setPadding(new Insets(20));
	    helpButtonPane.getChildren().add(helpText);
	    helpButtonPane.setStyle("-fx-background-color: orange; -fx-background-radius: 40; -fx-border-color: grey; -fx-border-width: 10px; -fx-border-radius: 30;");
	    Scene helpButtonScene = new Scene(helpButtonPane);
	    helpButtonScene.setFill(Color.TRANSPARENT);
	    helpButtonStage.setScene(helpButtonScene);
	    
	    helpButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
            	helpButtonStage.show();
            } else {
            	helpButtonStage.hide();
            }
        });

		//backButton will cause the control of the application to go back to the main menu
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				saveAndExitGame();
				_gameWindow.setScene(_menuScene);
				_gameWindow.show();
			}
		});

		//Label that will display the winnings value to the user in the Games Module menu
		Label winnings = new Label();
		winnings.setText("Winnings: $" + _winnings);

		winnings.setPrefSize(200, 50);
		winnings.setMaxSize(240, 50);
		winnings.setPadding(new Insets(2, 10, 2, 24));
		winnings.setAlignment(Pos.CENTER);
		
		if (Main.colourBlindMode()) {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 1;-fx-background-color: #008837;-fx-text-fill: #ffffff");
		} else {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 1;-fx-background-color: #2A9600;-fx-text-fill: #ffffff");
		}


		StackPane backAllignment = new StackPane();
		backAllignment.getChildren().add(backButton);
		StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);

		//Bottom menu consists of the winnings amount and tbe back button
		HBox bottomMenu = new HBox();
		bottomMenu.setSpacing(150);
		bottomMenu.setAlignment(Pos.CENTER_LEFT);
		bottomMenu.getChildren().addAll(winnings, backAllignment, helpButton);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));

		//Overall layout for the Games Module scene/window
		BorderPane layout = new BorderPane();
		layout.setTop(topMenu);
		layout.setCenter(quesLayout);
		layout.setBottom(bottomMenu);

		_quesScene = new Scene(layout, 800, 600);

		_gameWindow.setScene(_quesScene);
		_gameWindow.show();
	}


	/**
	 * This method displays the question board for the user by using the data available on 
	 * the categories and questions, via the _questionBank instance. This method is also
	 * responsible for handling the requests when a user selects a question to answer.
	 */
	private void displayQuestions(HBox topMenu, GridPane quesLayout) {
		int i = 0;
		int j = 0;

		int categoriesDone = 0;//this variable enables the check to see if all questions
		//of all categories have been attempted or not

		//for each question from each category create a new button or label depending on
		//if the question has been attempted or not
		for (Category c : _questionBank.getCategoryList()) {

			int questionsDone = 0;//this variable enables the check to see if all questions
			//of a category have been attempted or not

			Question lowestValueQuestion = null;

			for (Question q : c.getQuestions()) {
				if (q.getValue() != 1 && q.getValue() != -1) {
					lowestValueQuestion = q;
					break;
				}
			}

			for (Question q : c.getQuestions()) {

				//if the question has been attempted already
				if (q.getValue() == 1 || q.getValue() == -1) {
					String doneColor = "#000000";

					//if the question was answered correctly the color of text 'Done' is 
					//green, else the color of text 'Done' is red
					if (q.getValue() == 1) {
						if (Main.colourBlindMode()) {
							doneColor = "#008837";
						} else {
							doneColor = "#0E9109";
						}	
					} else if (q.getValue() == -1) {
						if (Main.colourBlindMode()) {
							doneColor = "#7B3294";
						} else {
							doneColor = "#BC0808";
						}
					}

					//'Done' label is for a question that has already been attempted
					Label attemptedQues = new Label();
					attemptedQues.setText(Integer.toString(Math.abs((questionsDone + 1) * 100)));
					attemptedQues.setPrefSize(90, 50);
					attemptedQues.setStyle("-fx-font-size: 18;-fx-border-width: 1; -fx-text-fill: " + doneColor + ";-fx-border-color: " + doneColor);
					attemptedQues.setAlignment(Pos.CENTER);
					
					GridPane.setConstraints(attemptedQues, i, j);
					quesLayout.getChildren().add(attemptedQues);

					questionsDone++;

				} else if (lowestValueQuestion != null && q.equals(lowestValueQuestion) && (!(c.getCategoryName().equalsIgnoreCase("international") && (categoriesDone < 2)))){

					//Creation of button instance for a question
					Button button = new Button();
					button.setText(Integer.toString(q.getValue()));
					button.setPrefSize(90, 50);
					button.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
							+ "-fx-font-size: 18;");

					button.setOnAction(new EventHandler<ActionEvent>() {
						public void handle (ActionEvent e) {

							//Confirm with the user if they want the answer the question
							boolean confirm = ConfirmBox.displayConfirm("Question confirmation",
									"You picked category " + c.getCategoryName() + " for " + Integer.toString(q.getValue()) + ". \n"
											+ "Are you sure you want this question?");

							if (confirm) {
								askQuestion(c,q);
							}

						}
					});
					GridPane.setConstraints(button, i, j);
					quesLayout.getChildren().add(button);
					
				} else {
					//Creation of label instance for a locked question
					Label lockedButton = new Label();
					lockedButton.setText(Integer.toString(q.getValue()));
					lockedButton.setPrefSize(90, 50);
					lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #d5e5f2");
					lockedButton.setPadding(new Insets(0, 0, 0, 23));

					if (c.getCategoryName().equalsIgnoreCase("international")) {
						lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #ddbdff");
					}
					
					GridPane.setConstraints(lockedButton, i, j);
					quesLayout.getChildren().add(lockedButton);
				}

				j++;
			}
			j = 0;
			i++;

			//Create a label for the category name
			Label categoryLabel = new Label();

			if (questionsDone == c.numberOfQuestions()) {

				categoryLabel.setText(c.getCategoryName());
				categoryLabel.setPrefSize(130, 50);
				categoryLabel.setMaxSize(130, 50);
				if (Main.colourBlindMode()) {
					categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 1;"
							+ "-fx-background-color: #008837;-fx-text-fill: #ffffff");
				} else {
					categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 1;"
							+ "-fx-background-color: #2A9600;-fx-text-fill: #ffffff");
				}
				categoriesDone++;
			} else {
				categoryLabel.setText(c.getCategoryName());
				categoryLabel.setPrefSize(130, 50);
				categoryLabel.setStyle("-fx-font-size: 15;-fx-border-width: 1; -fx-background-color: #040662;-fx-text-fill: #ffffff");
			}

			if (c.getCategoryName().equalsIgnoreCase("international")) {
				categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 1;"
						+ "-fx-background-color: #681773;-fx-text-fill: #ffffff");
			}
			
			categoryLabel.setAlignment(Pos.CENTER);

			topMenu.getChildren().add(categoryLabel);

			if (categoriesDone > 5) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						gameFinished();
					}
				});
			}
		}

	}

	/**
	 * This method given the Category and the Question, will ask the question in a new QuestionBox
	 * dialog. When the question is done answering it will read the user's answer input and evaluate
	 * if the answer was correct (matched the specified answer for the given Question instance. It
	 * will use this information to provide the appropriate feedback to the user.
	 */
	private void askQuestion(Category c, Question q) {

		//ask the user the question in a new QuestionBox window
		String answerInput = QuestionBox.displayConfirm("You picked category " + c.getCategoryName() +
				" for " + Integer.toString(q.getValue()), q.getQuestion(), q.getQuestion(), q.getClue(), false);

		//if the answer is correct, send an alert box to the user and update winnings
		if (q.answerValid(answerInput)) {
			_winnings += q.getValue();
			questionFeedback(true, q);

			//if the answer is wrong, send an alert box to the user and the total winnings stay the same
		} else {
			questionFeedback(false, q);
		}
	}

	/**
	 * This method creates a new window AlertBox to give the user feedback about 
	 * their answer to a question.
	 * @param outcome: true if answer was correct, false if answer was incorrect
	 * @param ques: the question that the user answered
	 */
	private void questionFeedback(boolean outcome, Question ques) {
		if (outcome) {
			if (Main.colourBlindMode()) {
				AlertBox.displayAlert("Correct answer", "Congratulations!!! You just won $" + ques.getValue(), "#008837");
			} else {
				AlertBox.displayAlert("Correct answer", "Congratulations!!! You just won $" + ques.getValue(), "#0E9109");
			}
		} else {

			if (Main.colourBlindMode()) {
				AlertBox.displayAlert("Incorrect answer", "Oops that was the wrong answer.", "#7B3294");
			} else {
				AlertBox.displayAlert("Incorrect answer", "Oops that was the wrong answer.", "#BC0808");
			}
		}
		ques.questionAttempted(outcome);
		displayQuestionBoard();
	}

	/**
	 * This method deletes any previous save_data recursively and then rewrites 
	 * the question/category data and the winnings data into the save_data directory.
	 */
	private void saveAndExitGame() {

		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "save_data";
		Path pathCategoryData = Paths.get(save_loc + System.getProperty("file.separator") + "categories");

		try {
			Files.createDirectories(pathCategoryData);


		} catch (IOException e) {
			System.err.println("Failed to create directory!" + e.getMessage());
		}

		try {
			FileWriter winningsWriter = new FileWriter(save_loc + "/winnings");
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
		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "save_data";
		File save_data = new File(save_loc);

		deleteDirectory(save_data);
	}

	/**
	 * This method displays a message with the total winnings earned by the user
	 * when the game is finished, i.e. when all the questions from all the categories
	 * have been attempted.
	 */
	private void gameFinished() {

		if (_winnings >= 4500) {
			AlertBox.displayAlert("Game finished", "Congratulations!!! You earned $" + _winnings + ". Check your grand prize from the main menu.", "#067CA0");
		}
		else {
			int prize = 4500 - _winnings;
			AlertBox.displayAlert("Game finished", "Congratulations!!! You earned $" + _winnings + ". Unfortunately, you were $" + String.valueOf(prize) +
					" off the grand prize.", "#067CA0");
		}
		
		_gameWindow.setScene(_menuScene);
		_gameWindow.show();
		
		boolean playAgain = ConfirmBox.displayConfirm("Play Again", "Would you like to play again?");
		
		if (playAgain) {
			AlertBox.displayAlert("New Game", "A new game has been created, come back to the Games Module to play again", "#000000");
			resetGame();
			Main.restartGame(_gameWindow);
		} else {
			saveAndExitGame();
		}

	}

	private void categorySelector() {

		HBox selectMenuLayout = new HBox();

		//Set up the layout for the list displaying all the selected categories
		VBox selectedCategoryDisplay = new VBox();
		selectedCategoryDisplay.setSpacing(10);
		selectedCategoryDisplay.setPadding(new Insets(20, 20, 30, 20)); 

		Text selection = new Text("Selected Categories:");
		selection.setFill(Color.ORANGE);
		selection.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
		selection.setTextAlignment(TextAlignment.CENTER);
		selectedCategoryDisplay.getChildren().add(selection);

		//Set up the layout for the contents of the main menu
		VBox categorySelectLayout = new VBox();
		categorySelectLayout.setSpacing(10);
		categorySelectLayout.setPadding(new Insets(20, 20, 30, 20)); 

		Text infoText = new Text("Please select five\n categories: ");
		infoText.setStyle("-fx-font-size: 18;");
		infoText.setTextAlignment(TextAlignment.CENTER);
		categorySelectLayout.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);


		List<String> selectedCategories = new ArrayList<String>();

		String categoryDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "categories";
		File directory = new File(categoryDir);
		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File x : fList) {

			if (!(x.getName().equalsIgnoreCase("international"))) {

				//Creation of button instance for a category
				Button categoryButton = new Button();
				categoryButton.setText(x.getName());
				categoryButton.setPrefSize(200, 40);
				categoryButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
						+ "-fx-font-size: 18;");

				categoryButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle (ActionEvent e) {
						for (String selected : selectedCategories) {
							if (x.getName().equalsIgnoreCase(selected)) {
								return;
							}
						}

						selectedCategories.add(x.getName());

						//Create a new label for the category selected by the user
						Label selectedCategoryLabel = new Label();
						selectedCategoryLabel.setText(x.getName());
						selectedCategoryLabel.setPrefSize(200, 40);
						selectedCategoryLabel.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
								+ "-fx-font-size: 18;");
						selectedCategoryLabel.setAlignment(Pos.CENTER);
						
						selectedCategoryDisplay.getChildren().addAll(selectedCategoryLabel);

						if (selectedCategories.size() == 5) {
							for (String d : selectedCategories) {
								System.out.print(d + ", ");
							}
							removeUnselectedCategories(selectedCategories);
							removeExtraQuestions();
							assignQuestionValues();
							displayQuestionBoard();
						}
					}
				});
				categorySelectLayout.getChildren().addAll(categoryButton);
			}
		}

		selectMenuLayout.getChildren().addAll(categorySelectLayout, selectedCategoryDisplay);

		//backButton will cause the control of the application to go back to the main menu
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_gameWindow.setScene(_menuScene);
				_gameWindow.show();
			}
		});

		//Bottom menu consists of the winnings amount and tbe back button
		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(backButton);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(backButton, Pos.CENTER);

		//Overall layout for the Games Module scene/window
		BorderPane layout = new BorderPane();
		layout.setCenter(selectMenuLayout);
		layout.setBottom(bottomMenu);

		Scene _categorySelectScene = new Scene(layout, 500, 600);
		_gameWindow.setScene(_categorySelectScene);
		_gameWindow.show();

	}

	private void removeUnselectedCategories(List<String> selectedCategories) {

		boolean categorySelected;

		List<Category> toRemove = new ArrayList<Category>();

		for (Category c : _questionBank.getCategoryList()) {
			categorySelected = false;

			for (String s : selectedCategories) {
				if ((c.getCategoryName().equalsIgnoreCase(s)) || (c.getCategoryName().equalsIgnoreCase("International"))) {
					categorySelected = true;
					break;
				}
			}

			if (!categorySelected) {
				toRemove.add(c);
			}
		}

		for (Category c : toRemove) {
			_questionBank.getCategoryList().remove(c);
		}

		moveInternationalToEnd();
	}

	private void moveInternationalToEnd() {
		Category international = _questionBank.getCategoryList().get(0); 

		for (Category c : _questionBank.getCategoryList()) {
			if (c.getCategoryName().equalsIgnoreCase("international")) {
				international = c;
			}
		}
		_questionBank.getCategoryList().remove(international);
		_questionBank.getCategoryList().add(international);
	}

	/**
	 * 
	 */
	private void removeExtraQuestions() {
		//until the number of questions in a particular category is not equal to 5, randomly remove a question
		for (Category c : _questionBank.getCategoryList()) {
			int numberOfQuestions = c.getQuestions().size();
			while (numberOfQuestions > 5) {
				int randomQuestion = generateRandomNumber(0, numberOfQuestions);
				c.getQuestions().remove(randomQuestion);
				numberOfQuestions = c.getQuestions().size();
			}
		}
	}

	private void assignQuestionValues() {
		//Assign the value of the questions for each category randomly from 100, 200, 300, 400, 500.
		int value;
		for (Category c : _questionBank.getCategoryList()) {
			value = 100;
			for (Question q : c.getQuestions()) {
				q.setValue(value);
				value += 100;
			}
		}
	}
}
