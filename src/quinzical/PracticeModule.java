package quinzical;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import quinzical.HelperThread;
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

public class PracticeModule {
	
	private Stage _stage;
	private Scene _menuScene;
	private QuestionRetriever _questions;
	
	public PracticeModule(Stage primaryStage, QuestionRetriever questions, Scene menuScene) {
		_stage = primaryStage;
		_questions = questions;
		_menuScene = menuScene;
	}
	public void practiceModule() {
		
//		VBox menuLayout = new VBox();
//		menuLayout.setSpacing(10);
//		menuLayout.setAlignment(Pos.CENTER);
//		menuLayout.setPadding(new Insets(20, 20, 30, 20)); 
//		Scene _menuScene = new Scene(menuLayout, 700, 450);
		
		
		displayCategoryBoard();
	}
	private void displayCategoryBoard() {
		
		StackPane menuInfo = new StackPane();
		Text infoText = new Text("Please select one of the following options: ");
		infoText.setStyle("-fx-font-size: 15;");
		infoText.setTextAlignment(TextAlignment.CENTER);
		menuInfo.setPadding(new Insets(20, 0, 0, 0));
		menuInfo.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		Random rand = new Random();
		
		//initial layout for the graphical layout of the category names
		VBox topMenu = new VBox();
		topMenu.setAlignment(Pos.CENTER);
		topMenu.setPadding(new Insets(5, 5, 5, 5));
		topMenu.setSpacing(8);
		
		for (Category category : _questions.getCategoryList()) {
			
			Button button = new Button();
			button.setText(category.getCategoryName());
			button.setPrefSize(200, 40);
			button.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
					+ "-fx-font-size: 18;");
			
			button.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					int randomQuestionIndex = rand.nextInt(category.numberOfQuestions());
					List<Question> questionList = category.getQuestions();
					int attempts = 0;
					//Confirm with the user if they want the answer the question
					boolean confirm = ConfirmBox.displayConfirm("Category confirmation",
							"You picked the " + category.getCategoryName() + " category.\nAre you sure you want this category?");
					
					if (confirm) {
						while (attempts < 3) {
							Question question = questionList.get(randomQuestionIndex);
							String answerInput = "";
							if (attempts == 2) {
								answerInput = QuestionBox.displayConfirm("You picked the " + category.getCategoryName() + " category", question.getQuestion(), question.getQuestion()
										+ "\n\nStarts with " + question.getAnswer().charAt(0));
							}
							else {
								//ask the user the question in a new QuestionBox window
								answerInput = QuestionBox.displayConfirm("You picked the " + category.getCategoryName() + " category", question.getQuestion(), question.getQuestion());
							}
							
						
						
							//if the answer is correct 'echo correct' using BASH, send an alert box to the user and update winnings
							if (answerInput.trim().equalsIgnoreCase(question.getAnswer())) {
								questionFeedback(true, question, false);
								break;
								//if the answer is wrong 'echo' the correct answer using BASH, send	an alert box to the user and update winnings
							} else {
							
								attempts += 1;
								if (attempts == 3) {
									questionFeedback(false, question, true);
								
								}
								else {
									questionFeedback(false, question, false);
								}
								
							}
						}
					} 
					
				}
			});
			topMenu.getChildren().add(button);
		}
		Button backButton = new Button("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton.setTextAlignment(TextAlignment.CENTER);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_stage.setScene(_menuScene);
			}
		});

		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(backButton);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(backButton, Pos.CENTER);

		BorderPane layout = new BorderPane();
		layout.setTop(menuInfo);
		layout.setCenter(topMenu);
		layout.setBottom(bottomMenu);

		Scene quesScene = new Scene(layout, 500, 500);

		_stage.setScene(quesScene);
	}
	private void questionFeedback(boolean outcome, Question ques, boolean fullyAttempted) {
		if (outcome) {
			AlertBox.displayAlert("Correct answer", "Correct!!!", "#0E9109");
		} else {
			if (!fullyAttempted) {
				AlertBox.displayAlert("Incorrect answer", "Incorrect, Try again!", "#BC0808");
			}
			else {
				AlertBox.displayAlert("Incorrect answer", "Incorrect, " + "The correct answer to '" + ques.getQuestion() + "' is " + ques.getAnswer(), "#BC0808");
			}
		}
		_stage.setScene(_menuScene);
	}
}
