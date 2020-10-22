package application.game;

import application.Main;
import application.helper.ConfirmBox;
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

public class GamesModuleComponents {
	
	static HBox getTopMenu() {
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(5, 5, 5, 5));
		topMenu.setSpacing(8);
		
		return topMenu;
	}
	
	static GridPane getQuesLayout() {
		GridPane quesLayout = new GridPane();
		quesLayout.setPadding(new Insets(20, 20, 20, 20));
		quesLayout.setVgap(20);
		quesLayout.setHgap(50);
		
		return quesLayout;
	}

	public static Button getHelpButton(Stage gameWindow) {
		Button helpButton = new Button("?");
		double r = 20;
		helpButton.setShape(new Circle(r));
		helpButton.setMinSize(2*r, 2*r);
		helpButton.setMaxSize(2*r, 2*r);
		helpButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		helpButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: #F0F8FF");
		
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
	
	private static Stage getHelpButtonStage(Stage gameWindow) {
		
		Text helpText = new Text("Help #1: You can only attempt the lowest value question of each category"
				+ "\n\nHelp #2: International category will be unlocked after you have attempted at least two categories\n\n"
				+ "Help #3: Your grand prize will be unlocked and available to be viewed from the main menu once you earn at least $4500");
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
	
	public static Button getBackButton() {
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		
		return backButton;
	}
	
	public static Label getWinningsLabel(String text) {
		
		Label winnings = new Label();
		winnings.setText(text);

		winnings.setPrefHeight(50);
		winnings.setPadding(new Insets(2, 10, 2, 24));
		winnings.setAlignment(Pos.CENTER);
		
		if (Main.colourBlindMode()) {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 1;-fx-background-color: #008837;-fx-text-fill: #ffffff");
		} else {
			winnings.setStyle("-fx-font-size: 20;-fx-border-width: 1;-fx-background-color: #2A9600;-fx-text-fill: #ffffff");
		}
		
		return winnings;
	}
	
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
		Label attemptedQues = new Label();
		attemptedQues.setText(Integer.toString(value));
		attemptedQues.setPrefSize(90, 50);
		attemptedQues.setStyle("-fx-font-size: 18;-fx-border-width: 1; -fx-text-fill: " + doneColor + ";-fx-border-color: " + doneColor);
		attemptedQues.setAlignment(Pos.CENTER);
		
		return attemptedQues;
	}
	
	public static Button getAvailableQuesButton(Category c, Question q, GamesModule game) {
		//Creation of button instance for a question
		Button unlockedQuesButton = new Button();
		unlockedQuesButton.setText(Integer.toString(q.getValue()));
		unlockedQuesButton.setPrefSize(90, 50);
		unlockedQuesButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
				+ "-fx-font-size: 18;");

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
	
	public static Label getLockedQuesLabel(Category c, Question q) {
		
		//Creation of label instance for a locked question
		Label lockedButton = new Label();
		lockedButton.setText(Integer.toString(q.getValue()));
		lockedButton.setPrefSize(90, 50);
		lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #d5e5f2");
		lockedButton.setPadding(new Insets(0, 0, 0, 23));

		if (c.getCategoryName().equalsIgnoreCase("international")) {
			lockedButton.setStyle("-fx-border-width: 2; -fx-font-size: 18; -fx-background-color: #f7e1b2");
		}
		
		return lockedButton;
	}
	
	public static Label getCompletedCategoryLabel(Category c) {
		Label categoryLabel = new Label();
		
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
		
		return categoryLabel;
	}
	
	public static Label getIncompletCategoryLabel(Category c) {
		Label categoryLabel = new Label();
		
		categoryLabel.setText(c.getCategoryName());
		categoryLabel.setPrefSize(130, 50);
		categoryLabel.setStyle("-fx-font-size: 15;-fx-border-width: 1; -fx-background-color: #040662;-fx-text-fill: #ffffff");
		
		return categoryLabel;
	}

}

