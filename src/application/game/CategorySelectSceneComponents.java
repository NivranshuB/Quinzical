package application.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.helper.GlossButton;
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

	private static Button okButton = getOkButton();
	
	static VBox getSelectedCategoryDisplay() {

		//Set up the layout for the list displaying all the selected categories
		VBox selectedCategoryDisplay = new VBox();
		selectedCategoryDisplay.setSpacing(10);
		selectedCategoryDisplay.setPadding(new Insets(20, 20, 30, 20)); 

		Label selection = new Label("Selected Categories:");

		selection.setPrefSize(200, 40);
		selection.setTextFill(Color.BROWN);
		selection.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
		selection.setTextAlignment(TextAlignment.CENTER);
		selection.setStyle("-fx-stroke: #2b1600;-fx-stroke-width: 1px;");
		selectedCategoryDisplay.getChildren().add(selection);

		return selectedCategoryDisplay;
	}

	static VBox getCategorySelectLayout() {

		VBox categorySelectLayout = new VBox();
		categorySelectLayout.setSpacing(10);
		categorySelectLayout.setPadding(new Insets(20, 20, 30, 20)); 

		Text infoText = new Text("Please select five\n categories: ");
		infoText.setFill(Color.BROWN);
		infoText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
		infoText.setStyle("-fx-stroke: #2b1600;-fx-stroke-width: 0px;");
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

						if (selectedCategories.size() > 4) {
							return;
						}
						
						selectedCategories.add(x.getName());

						//Create a new label for the category selected by the user
						Button selectedCategory = new Button();
						selectedCategory.setText(x.getName());
						selectedCategory.setPrefSize(200, 40);
						selectedCategory.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-background-color: #f7a725;"
								+ "-fx-font-size: 18;");
						selectedCategory.setOpacity(0.7);
						selectedCategory.setAlignment(Pos.CENTER);
						selectedCategory.setOnAction(new EventHandler<ActionEvent>() {
							public void handle (ActionEvent e) {
								selectedCategoryDisplay.getChildren().remove(selectedCategory);
								String categoryToRemove = x.getName();
								for (String selected : selectedCategories) {
									if (x.getName().equalsIgnoreCase(selected)) {
										categoryToRemove = selected;
									}
								}
								selectedCategories.remove(categoryToRemove);
								if (selectedCategoryDisplay.getChildren().contains(okButton) && selectedCategories.size() < 5) {
									selectedCategoryDisplay.getChildren().remove(okButton);
								}
							}
						});
						selectedCategoryDisplay.getChildren().addAll(selectedCategory);
					
						if (selectedCategories.size() == 5) {
							selectedCategoryDisplay.getChildren().addAll(okButton);
							okButton.setOnAction(new EventHandler<ActionEvent>() {
								public void handle (ActionEvent e) {
									if (selectedCategories.size() == 5) {
										CategorySelectScene.removeUnselectedCategories(selectedCategories, questionBank);
										CategorySelectScene.removeExtraQuestions(questionBank);
										CategorySelectScene.assignQuestionValues(questionBank);
										gameScene.displayQuestionBoard();	
									}
								}
							});
							okButton =  GlossButton.addGlossEffect(okButton, 18);
						}

					}
				});
				categorySelectLayout.getChildren().addAll(categoryButton);
			}
		}

	}

	static List<String> removeSelectedCategory(File removedCategory, List<String> selectedCategories) {
		for (String selected : selectedCategories) {
			if (removedCategory.getName().equalsIgnoreCase(selected)) {
				selectedCategories.remove(selected);
			}
		}
		return selectedCategories;
	}
	
	static Button getOkButton() {
		Button okButton = new Button();
		okButton.setText("ok");
		okButton.setPrefSize(180, 40);
		okButton.setStyle("-fx-border-color: #33b3de;-fx-border-width: 1;-fx-background-color: #33b3de;-fx-font-size: 18;");

		return okButton;
	}

}
