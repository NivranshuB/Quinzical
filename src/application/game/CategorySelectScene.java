package application.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.helper.RandomNumber;
import application.questions.Category;
import application.questions.Question;
import application.questions.QuestionBank;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CategorySelectScene {

	/**
	 * This method starts the category select scene which allows the user to pick 5 categories
	 * they would like questions from. This scene is only provided at the start of a new game.
	 */
	public static void categorySelector(Stage window, QuestionBank questionBank, Scene menuScene, GamesModule gameScene) {
		
		HBox selectMenuLayout = new HBox();

		//Set up the layout for the list displaying all the selected categories
		VBox selectedCategoryDisplay = CategorySelectSceneComponents.getSelectedCategoryDisplay();

		//Set up the layout for all the categories available for selection
		VBox categorySelectLayout = CategorySelectSceneComponents.getCategorySelectLayout();

		CategorySelectSceneComponents.addSelectedAndUnselectedCategories(questionBank, gameScene, selectedCategoryDisplay,
				categorySelectLayout);
		
		selectMenuLayout.getChildren().addAll(categorySelectLayout, selectedCategoryDisplay);

		//backButton will cause the control of the application to go back to the main menu
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				window.setScene(menuScene);
				window.show();
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
		window.setScene(_categorySelectScene);
		window.show();

	}
	
	/**
	 * Given a list of String, where each string is a category name, this method removes 
	 * every category that is not in the input list from the _questionBank.
	 * 
	 * @param selectedCategories
	 */
	static void removeUnselectedCategories(List<String> selectedCategories, QuestionBank questionBank) {

		boolean categorySelected;

		List<Category> toRemove = new ArrayList<Category>();

		for (Category c : questionBank.getCategoryList()) {
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
			questionBank.getCategoryList().remove(c);
		}

		moveInternationalToEnd(questionBank);
	}
	
	/**
	 * This method randomly removes a question from a category in the _questionBank until every
	 * category in the _questionBank has exactly 5 Questions each.
	 */
	static void removeExtraQuestions(QuestionBank questionBank) {
		//until the number of questions in a particular category is not equal to 5, randomly remove a question
		for (Category c : questionBank.getCategoryList()) {
			int numberOfQuestions = c.getQuestions().size();
			while (numberOfQuestions > 5) {
				int randomQuestion = RandomNumber.generateBetween(0, numberOfQuestions);
				c.getQuestions().remove(randomQuestion);
				numberOfQuestions = c.getQuestions().size();
			}
		}
	}
	
	/**
	 * This method moves the 'International' question category in the _questionBank to the last
	 * category in the _questionBank.
	 */
	public static void moveInternationalToEnd(QuestionBank questionBank) {
		Category international = questionBank.getCategoryList().get(0); 

		for (Category c : questionBank.getCategoryList()) {
			if (c.getCategoryName().equalsIgnoreCase("international")) {
				international = c;
			}
		}
		questionBank.getCategoryList().remove(international);
		questionBank.getCategoryList().add(international);
	}
	
	/**
	 * This method assigns the questions in the _questionBank appropriately.
	 */
	static void assignQuestionValues(QuestionBank questionBank) {
		//Assign the value of the questions for each category randomly from 100, 200, 300, 400, 500.
		int value;
		for (Category c : questionBank.getCategoryList()) {
			value = 100;
			for (Question q : c.getQuestions()) {
				q.setValue(value);
				value += 100;
			}
		}
	}
}
