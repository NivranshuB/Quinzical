package application.game;

import application.Main;
import application.helper.AlertBox;
import application.questions.Category;
import application.questions.Question;
import application.questions.QuestionBank;
import application.questions.QuestionBox;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class QuestionGrid {

	/**
	 * This method displays the question board for the user by using the data available on 
	 * the categories and questions, via the _questionBank instance. This method is also
	 * responsible for handling the requests when a user selects a question to answer.
	 */
	public static void displayQuestions(HBox topMenu, GridPane quesLayout, GamesModule game, QuestionBank questionBank) {
		int i = 0;
		int j = 0;

		int categoriesDone = 0;//this variable enables the check to see if all questions
		//of all categories have been attempted or not

		//for each question from each category create a new button or label depending on
		//if the question has been attempted or not
		for (Category c : questionBank.getCategoryList()) {

			int questionsDone = 0;//this categoryLabel.setPrefSize(130,variable enables the check to see if all questions
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

					//Label for a question that has already been attempted
					Label attemptedQues = GamesModuleComponents.getAttemptedQuesLabel(Math.abs((questionsDone + 1) * 100)
							, Main.colourBlindMode(), q.getValue());

					GridPane.setConstraints(attemptedQues, i, j);
					quesLayout.getChildren().add(attemptedQues);

					questionsDone++;

				} else if (lowestValueQuestion != null && q.equals(lowestValueQuestion) &&
						(!(c.getCategoryName().equalsIgnoreCase("international") && (categoriesDone < 2)))){

					//Creation of button instance for a question
					Button unlockedQuesButton = GamesModuleComponents.getAvailableQuesButton(c, q, game);
					GridPane.setConstraints(unlockedQuesButton, i, j);
					quesLayout.getChildren().add(unlockedQuesButton);

				} else {
					//Creation of label instance for a locked question
					Label lockedButton = GamesModuleComponents.getLockedQuesLabel(c, q);

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
				categoryLabel = GamesModuleComponents.getCompletedCategoryLabel(c);
				categoriesDone++;
			} else {
				categoryLabel = GamesModuleComponents.getIncompletCategoryLabel(c);
			}

			if (c.getCategoryName().equalsIgnoreCase("international")) {
				categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 1;"
						+ "-fx-background-color: #cf5a00;-fx-text-fill: #ffffff");
			}

			categoryLabel.setAlignment(Pos.CENTER);

			topMenu.getChildren().add(categoryLabel);

			if (categoriesDone > 5) {
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						
						if (!Main._scoreboard.gameFinished()) {
							int winnings = game.getWinnings();
							if (winnings >= 4500) {
								Main._scoreboard.addToScoreboard("Game finished", "Congratulations!!! You earned $" + winnings 
										+ ". Check your grand prize from the main menu.", "#067CA0", winnings);
							}
							else {
								int prize = 4500 - winnings;
								Main._scoreboard.addToScoreboard("Game finished", "Congratulations!!! You earned $" + winnings + ". Unfortunately, you were $" + String.valueOf(prize) +
										" off the grand prize.", "#067CA0", winnings);
							}
							Main._scoreboard.setGameFinished(true);
						}
						RewardScreen.displayRewardScreen(game.getWinnings());
						game.gameFinished();
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
	static void askQuestion(Category c, Question q, GamesModule game) {

		//ask the user the question in a new QuestionBox window
		String answerInput = QuestionBox.displayConfirm("You picked category " + c.getCategoryName() +
				" for " + Integer.toString(q.getValue()), q.getQuestion(), q.getQuestion(), q.getClue(), false);
		if (answerInput.equals("Ran out of time!")) {
			questionFeedback(false, q, "Ran out of time!", game);
		} else if (answerInput.equals("Its fine you don't know!")) {
			questionFeedback(false, q, "Its fine you don't know!", game);
		} else if (answerInput.equals("The question you selected is now considered as attempted")) {
			questionFeedback(false, q, "The question you selected is now considered as attempted", game);
		} else {
			//if the answer is correct, send an alert box to the user and update winnings
			if (q.answerValid(answerInput)) {
				game.setWinnings(game.getWinnings() + q.getValue());
				questionFeedback(true, q, "Congratulations!!! You just won $" + q.getValue(), game);

				//if the answer is wrong, send an alert box to the user and the total winnings stay the same
			} else {
				questionFeedback(false, q, "Oops that was the wrong answer.", game);
			}
		}
	}
	
	/**
	 * This method creates a new window AlertBox to give the user feedback about 
	 * their answer to a question.
	 * @param outcome: true if answer was correct, false if answer was incorrect
	 * @param ques: the question that the user answered
	 */
	private static void questionFeedback(boolean outcome, Question ques, String feedback, GamesModule game) {
		if (outcome) {
			if (Main.colourBlindMode()) {
				AlertBox.displayAlert("Correct answer", feedback, "#008837");
			} else {
				AlertBox.displayAlert("Correct answer", feedback, "#0E9109");
			}
		} else {

			if (Main.colourBlindMode()) {
				if (feedback.equals("Ran out of time!")) {
					AlertBox.displayAlert("Time ran out", feedback, "#7B3294");
				} else if (feedback.equals("Its fine you don't know!")) {
					AlertBox.displayAlert("Don't know", feedback, "#7B3294");
				} else if (feedback.equals("The question you selected is now considered as attempted")) {
					AlertBox.displayAlert("Attempted", feedback, "#7B3294");
				} else {
					AlertBox.displayAlert("Incorrect answer", feedback, "#7B3294");
				}
			} else {
				if (feedback.equals("Ran out of time!")) {
					AlertBox.displayAlert("Time ran out", feedback, "#BC0808");
				} else if (feedback.equals("Its fine you don't know!")) {
					AlertBox.displayAlert("Don't know", feedback, "#BC0808");
				} else if (feedback.equals("The question you selected is now considered as attempted")) {
					AlertBox.displayAlert("Attempted", feedback, "#BC0808");
				} else {
					AlertBox.displayAlert("Incorrect answer", feedback, "#BC0808");
				}
			}
		}
		ques.questionAttempted(outcome);
		game.displayQuestionBoard();
	}
}
