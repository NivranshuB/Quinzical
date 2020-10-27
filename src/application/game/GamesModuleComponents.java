package application.game;

import application.Main;
import application.helper.ConfirmBox;
import application.helper.GlossButton;
import application.questions.Category;
import application.questions.Question;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class contains methods to set up the GUI components for the games module
 * @author team 41
 */

public class GamesModuleComponents {
	
	/**
	 * This method creates and sets the constraints for the headings box
	 * @return headings box
	 */
	static HBox getTopMenu() {
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(5, 5, 5, 5));
		topMenu.setSpacing(8);
		
		return topMenu;
	}
	/**
	 * This method creates and sets the constraints for the question grid
	 * @return grid pane for games module questions
	 */
	static GridPane getQuesLayout() {
		GridPane quesLayout = new GridPane();
		quesLayout.setPadding(new Insets(20, 20, 20, 20));
		quesLayout.setVgap(20);
		quesLayout.setHgap(50);
		
		return quesLayout;
	}
	/**
	 * This method creates the help button for games module
	 * @return help button with hover over functionality
	 */
	public static Button getHelpButton(Stage gameWindow) {
		Button helpButton = new Button("?");
		double r = 20;
		helpButton.setShape(new Circle(r));
		helpButton.setMinSize(2*r, 2*r);
		helpButton.setMaxSize(2*r, 2*r);
		helpButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		helpButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: #F0F8FF");
		
		//Get the stage for displaying the help text
		Stage helpButtonStage = GamesModuleComponents.getHelpButtonStage(gameWindow);

		//Display help text when hovering over button
		helpButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				helpButtonStage.show();
			} else {
				helpButtonStage.hide();
			}
		});
		
		return helpButton;
	}
	/**
	 * This method creates the text and stage for the help button
	 * @return stage of help text
	 */
	private static Stage getHelpButtonStage(Stage gameWindow) {
		
		Text helpText = new Text("Help #1: You can only attempt the lowest value question of each category"
				+ "\n\nHelp #2: International category will be unlocked after you have attempted at least two full categories\n\n"
				+ "Help #3: Hover over an attempted question to see what its value was and if you got it right or wrong\n\n"
				+ "Help #4: Get as many questions right as possible to win different kiwi style rewards!");
		helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Stage helpButtonStage = new Stage();
		helpButtonStage.initOwner(gameWindow);
		helpButtonStage.initStyle(StageStyle.TRANSPARENT); //Makes stage transparent to make scene look round
		
		StackPane helpButtonPane = new StackPane();
		helpButtonPane.setPadding(new Insets(20));
		helpButtonPane.getChildren().add(helpText);
		helpButtonPane.setStyle("-fx-background-color: orange; -fx-background-radius: 40; -fx-border-color: grey;"
				+ " -fx-border-width: 10px; -fx-border-radius: 30;");
		
		Scene helpButtonScene = new Scene(helpButtonPane);
		helpButtonScene.setFill(Color.TRANSPARENT);
		helpButtonStage.setScene(helpButtonScene);
		
		return helpButtonStage;
	}
	/**
	 * This method creates the back button then call the static addGlossEffect method
	 * @return back button with gloss effect
	 */
	public static Button getBackButton() {
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton = GlossButton.addGlossEffect(backButton, 18);
		
		return backButton;
	}
	/**
	 * This method creates the winnings label to display in games module
	 * @return winnings label
	 */
	public static Label getWinningsLabel(String text) {
		
		Label winnings = new Label();
		winnings.setText(text);

		winnings.setPrefHeight(50);
		winnings.setPadding(new Insets(2, 10, 2, 24));
		winnings.setAlignment(Pos.CENTER);
		
		//If colour blind mode enabled then use different shade of green
		if (Main.colourBlindMode()) {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 0;-fx-background-color: #008837;"
					+ "-fx-text-fill: #ffffff;-fx-border-color: #ffffff");
		} else {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 0;-fx-background-color: #2A9600;"
					+ "-fx-text-fill: #ffffff;-fx-border-color: #ffffff");
		}
		
		return winnings;
	}
	/**
	 * This method creates and sets up constraints for the label of an attempted question
	 * @return label of attempted question
	 */
	public static Label getAttemptedQuesLabel(int value, boolean ColourBlindMode, int qOutcome) {
		String doneColor = "#000000";

		//if the question was answered correctly the color of text 'Done' is 
		//green, else the color of text 'Done' is red
		if (qOutcome == 1) {
			if (Main.colourBlindMode()) {
				doneColor = "#008837";
			} else {
				doneColor = "#0E9109";
			}	
		} else if (qOutcome == -1) {
			if (Main.colourBlindMode()) {
				doneColor = "#7B3294";
			} else {
				doneColor = "#BC0808";
			}
		}
		//Set constraints of label
		Label attemptedQues = new Label();
		attemptedQues.setText(Integer.toString(value));
		attemptedQues.setPrefSize(90, 50);
		attemptedQues.setStyle("-fx-font-size: 18;-fx-border-width: 1; -fx-text-fill: #ffffff;-fx-background-radius: 15;"
				+ "-fx-background-color: " + doneColor);
		attemptedQues.setOpacity(0);
		attemptedQues.setAlignment(Pos.CENTER);
		
		//Display question when hovering over button otherwise keep it transparent
		attemptedQues.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				attemptedQues.setOpacity(1.0);
			} else {
				attemptedQues.setOpacity(0);
			}
		});
		
		return attemptedQues;
	}
	/**
	 * This method creates and sets up constraints for button of an available question
	 * @return available question button
	 */
	public static Button getAvailableQuesButton(Category c, Question q, GamesModule game) {
		//Creation of button instance for a question
		Button unlockedQuesButton = new Button();
		unlockedQuesButton.setText(Integer.toString(q.getValue()));
		unlockedQuesButton.setPrefSize(90, 50);
		unlockedQuesButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
				+ "-fx-font-size: 18;");
		
		unlockedQuesButton = GlossButton.addGlossEffect(unlockedQuesButton, 18);

		unlockedQuesButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {

				//Confirm with the user if they want the answer the question
				boolean confirm = ConfirmBox.displayConfirm("Question confirmation",
						"You picked category " + c.getCategoryName() + " for " + Integer.toString(q.getValue()) + ". \n"
								+ "Are you sure you want this question?");
				if (confirm) {
					QuestionGrid.askQuestion(c, q, game);
				}
			}
		});
		return unlockedQuesButton;
	}
	/**
	 * This method creates and sets up constraints for the label of a locked question
	 * @return label of a locked question
	 */
	public static Label getLockedQuesLabel(Category c, Question q) {
		
		//Creation of label instance for a locked question
		Label lockedButton = new Label();
		lockedButton.setText(Integer.toString(q.getValue()));
		lockedButton.setPrefSize(90, 50);
		lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #d5e5f2; -fx-background-radius: 15");
		lockedButton.setPadding(new Insets(0, 0, 0, 23));

		if (c.getCategoryName().equalsIgnoreCase("international")) {
			lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #f7e1b2; -fx-background-radius: 15");
		}
		
		lockedButton.setOpacity(0.7);
		
		return lockedButton;
	}
	/**
	 * This method creates and sets up constraints for the header label of a completed category
	 * @return label of a completed category
	 */
	public static Label getCompletedCategoryLabel(Category c) {
		
		//Set constraints of label 
		Label categoryLabel = new Label();
		categoryLabel.setText(c.getCategoryName());
		categoryLabel.setPrefSize(130, 50);
		categoryLabel.setMaxSize(130, 50);
		
		//If colour blind mode is enabled then change shade of green
		if (Main.colourBlindMode()) {
			categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 2;-fx-border-color: #ffffff;"
					+ "-fx-background-color: #008837;-fx-text-fill: #ffffff");
		} else {
			categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 2;-fx-border-color: #ffffff;"
					+ "-fx-background-color: #2A9600;-fx-text-fill: #ffffff");
		}
		
		return categoryLabel;
	}
	/**
	 * This method creates and sets up constraints for the header label of an incomplete category
	 * @return label of an incomplete category
	 */
	public static Label getIncompleteCategoryLabel(Category c) {
		
		//Set constraints of label
		Label categoryLabel = new Label();
		categoryLabel.setText(c.getCategoryName());
		categoryLabel.setPrefSize(130, 50);
		categoryLabel.setStyle("-fx-font-size: 18;-fx-border-width: 2;-fx-border-color: #ffffff;"
				+ " -fx-background-color: #040662;-fx-text-fill: #ffffff");
		
		return categoryLabel;
	}

}

