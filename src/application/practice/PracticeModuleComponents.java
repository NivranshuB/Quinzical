package application.practice;

import java.util.List;

import application.helper.ConfirmBox;
import application.helper.GlossButton;
import application.questions.Category;
import application.questions.Question;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PracticeModuleComponents {

	static StackPane getMenuInfo() {
		
		StackPane menuInfo = new StackPane();
		Text infoText = new Text("Practice Module");
		infoText.setFill(Color.ORANGE);
		infoText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 36));
		infoText.setTextAlignment(TextAlignment.CENTER);
		infoText.setStyle("-fx-stroke: #2b1600;-fx-stroke-width: 1px;");
		infoText.setUnderline(true);
		menuInfo.setPadding(new Insets(10, 0, 15, 0));
		menuInfo.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		return menuInfo;
	}
	
	static VBox getTopMenu() {
		VBox topMenu = new VBox();
		topMenu.setAlignment(Pos.CENTER);
		topMenu.setPadding(new Insets(5, 5, 5, 5));
		topMenu.setSpacing(8);
		
		return topMenu;
	}
	
	static Button getCategoryButton(Category category, PracticeModule practice) {
		Button button = new Button();
		button.setText(category.getCategoryName());
		button.setPrefSize(200, 40);
		button.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;"
				+ "-fx-font-size: 18;");
		
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {

				List<Question> questionList = category.getQuestions();
				
				//Confirm with the user if they want the answer the question
				boolean confirm = ConfirmBox.displayConfirm("Category confirmation",
						"You picked the " + category.getCategoryName() + " category.\nAre you sure you want this category?");
				
				if (confirm) {
					practice.askQuestion(questionList, category);
				} 
			}
		});
		
		return button;
	}
	
	static Button getBackButton(Stage stage, Scene menuScene) {
		Button backButton = new Button("Back");
		backButton.setPrefSize(80, 40);
		backButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		backButton.setTextAlignment(TextAlignment.CENTER);
		backButton = GlossButton.addGlossEffect(backButton, 18);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				stage.setScene(menuScene);
			}
		});
		
		return backButton;
	}
	
	static StackPane getBottomMenu(Button backButton) {
		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(backButton);
		bottomMenu.setPadding(new Insets(10));
		StackPane.setAlignment(backButton, Pos.CENTER);
		
		return bottomMenu;
	}
}
