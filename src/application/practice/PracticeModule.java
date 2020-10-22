package application.practice;

import java.util.List;
import java.util.Random;

import application.Main;
import application.helper.AlertBox;
import application.helper.ConfirmBox;
import application.questions.Category;
import application.questions.Question;
import application.questions.QuestionBank;
import application.questions.QuestionBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Class that performs all the functionality of the practice module scene of the application.
 * @author Whan Jung
 *
 */
public class PracticeModule {
	
	private Stage _stage;
	private Scene _menuScene;
	private QuestionBank _questions;
	
	public PracticeModule(Stage primaryStage, QuestionBank questions, Scene menuScene) {
		_stage = primaryStage;
		_questions = questions;
		_menuScene = menuScene;
	}
	
	/**
	 * Method that displays the practice module menu
	 */
	public void practiceModule() {
			
		StackPane menuInfo = PracticeModuleComponents.getMenuInfo();
		
		//initial layout for the graphical layout of the category names
		VBox topMenu = PracticeModuleComponents.getTopMenu();
		
		//Create button for each category
		for (Category category : _questions.getCategoryList()) {
			Button button = PracticeModuleComponents.getCategoryButton(category, this);
			topMenu.getChildren().add(button);
		}
		
		Button backButton = PracticeModuleComponents.getBackButton(_stage, _menuScene);

		StackPane bottomMenu = PracticeModuleComponents.getBottomMenu(backButton);

		BorderPane layout = new BorderPane();
		layout.setTop(menuInfo);
		layout.setCenter(topMenu);
		layout.setBottom(bottomMenu);
		layout.setPadding(new Insets(20, 100, 30, 100));

		Scene quesScene = new Scene(layout, 800, 600);

		_stage.setScene(quesScene);
	}
	
	/**
	 * 
	 */
	void askQuestion(List<Question> questionList, Category selectedCategory) {
		
		Random rand = new Random();
		int attempts = 0;
		int randomQuestionIndex = rand.nextInt(selectedCategory.numberOfQuestions());

		while (attempts < 3) {
			Question question = questionList.get(randomQuestionIndex);
			String answerInput = "";
			if (attempts == 2) {
				//On third attempt, display hint
				answerInput = QuestionBox.displayConfirm("You picked the " + selectedCategory.getCategoryName() + " category"
						, question.getQuestion(), question.getQuestion()
						+ "\n\nHint: starts with " + question.getAnswer().charAt(0), question.getClue(), true);
			}
			else {
				//ask the user the question in a new QuestionBox window
				answerInput = QuestionBox.displayConfirm("You picked the " + selectedCategory.getCategoryName() + " category",
						question.getQuestion(), question.getQuestion(), question.getClue(), true);
			}
		
			//if the answer is correct, send an alert box to the user
			if (question.answerValid(answerInput)) {
				questionFeedback(true, question, false);
				break;
			} else {
			
				attempts += 1;
				//if the answer is wrong, send an alert box to the user
				if (attempts == 3) {
					questionFeedback(false, question, true);
				}
				else {
					questionFeedback(false, question, false);
				}
				
			}
		}
	
	}
	
	/**
	 * Method that alerts user with a pop-up window depending on the users answer
	 * @param outcome | if answer is correct or not
	 * @param ques | question
	 * @param fullyAttempted | if question attemped 3 times or not
	 *
	 */
	private void questionFeedback(boolean outcome, Question ques, boolean fullyAttempted) {
		if (outcome) {
			if (Main.colourBlindMode()) {
				AlertBox.displayAlert("Correct answer", "Correct!!!", "#008837");
			} else {
				AlertBox.displayAlert("Correct answer", "Correct!!!", "#0E9109");
			}
		} else {
			if (!fullyAttempted) {
				if (Main.colourBlindMode()) {
					AlertBox.displayAlert("Incorrect answer", "Incorrect. Try again!", "#7B3294");
				} else {
					AlertBox.displayAlert("Incorrect answer", "Incorrect. Try again!", "#BC0808");
				}
			}
			else {
				if (Main.colourBlindMode()) {
					AlertBox.displayAlert("Incorrect answer", "Incorrect. " + "The correct answer to '"
				+ ques.getQuestion() + "' is " + ques.getAnswer(), "#7B3294");
				} else {
					AlertBox.displayAlert("Incorrect answer", "Incorrect. " + "The correct answer to '"
				+ ques.getQuestion() + "' is " + ques.getAnswer(), "#BC0808");
				}
			}
		}
		_stage.setScene(_menuScene);
	}
}
