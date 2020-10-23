package application.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.questions.QuestionBank;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CategorySelectSceneComponents {

	static VBox getSelectedCategoryDisplay() {

		//Set up the layout for the list displaying all the selected categories
		VBox selectedCategoryDisplay = new VBox();
		selectedCategoryDisplay.setSpacing(10);
		selectedCategoryDisplay.setPadding(new Insets(20, 20, 30, 20)); 

		Label selection = new Label("Selected Categories:");
		selection.setPrefSize(200, 40);
		selection.setTextFill(Color.ORANGE);
		selection.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
		selection.setTextAlignment(TextAlignment.CENTER);
		selectedCategoryDisplay.getChildren().add(selection);
		
		return selectedCategoryDisplay;
	}
	
	static VBox getCategorySelectLayout() {
		
		VBox categorySelectLayout = new VBox();
		categorySelectLayout.setSpacing(10);
		categorySelectLayout.setPadding(new Insets(20, 20, 30, 20)); 

		Text infoText = new Text("Please select five\n categories: ");
		infoText.setStyle("-fx-font-size: 18;");
		infoText.setTextAlignment(TextAlignment.CENTER);
		categorySelectLayout.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		return categorySelectLayout;
	}
	
	static void addSelectedAndUnselectedCategories(QuestionBank questionBank, GamesModule gameScene,
			VBox selectedCategoryDisplay, VBox categorySelectLayout) {
		
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
							Button okButton = getOkButton();
							
							okButton.setOnAction(new EventHandler<ActionEvent>() {
								public void handle (ActionEvent e) {
									CategorySelectScene.removeUnselectedCategories(selectedCategories, questionBank);
									CategorySelectScene.removeExtraQuestions(questionBank);
									CategorySelectScene.assignQuestionValues(questionBank);
									gameScene.displayQuestionBoard();
								}
							});
							
							selectedCategoryDisplay.getChildren().addAll(okButton);

						}
					}
				});
				categorySelectLayout.getChildren().addAll(categoryButton);
			}
		}
	}
	
	static Button getOkButton() {
		Button okButton = new Button();
		okButton.setText("ok");
		okButton.setPrefSize(200, 40);
		okButton.setStyle("-fx-border-color: #33b3de;-fx-border-width: 1;-fx-background-color: #33b3de;-fx-font-size: 18;");

		return okButton;
	}

}
