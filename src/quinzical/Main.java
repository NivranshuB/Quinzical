package quinzical;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
	
	QuestionRetriever _questions;//all access to category and questions is through this
	//Winnings _currentWinnings;//allows the retrieval of saved winnings data
	Stage _gameWindow;
	Scene _menuScene;//the default scene for the game (also the main menu)
	//int _winnings;//the total winnings for the player
	
	@Override
	public void start(Stage primaryStage) {
		
		_questions = new QuestionRetriever();//initialise categories and questions
		_gameWindow = primaryStage;
		primaryStage.setTitle("Quinzical!");

		StackPane menuTitleText = new StackPane();
		Text welcomeText = new Text("Welcome to Quinzical");
		welcomeText.setFill(Color.ORANGE);
		welcomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		menuTitleText.getChildren().add(welcomeText);
		StackPane.setAlignment(welcomeText, Pos.CENTER);
		
		StackPane menuInfo = new StackPane();
		Text infoText = new Text("Please select one of the following options: ");
		infoText.setStyle("-fx-font-size: 15;");
		infoText.setTextAlignment(TextAlignment.CENTER);
		menuInfo.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		Button gamesModuleButton = new Button("Games Module");
		gamesModuleButton.setPrefSize(460,60);
		gamesModuleButton.setStyle("-fx-border-color: #200459;-fx-border-width: 1;-fx-font-size: 16;");
		gamesModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				GamesModule gameMenu = new GamesModule();
				gameMenu.start(_gameWindow);
			}
		});
		
		Button practiceModuleButton = new Button("Practice Module");
		practiceModuleButton.setPrefSize(460,60);
		practiceModuleButton.setStyle("-fx-border-color: #070459;-fx-border-width: 1;-fx-font-size: 16;");
		
		
		Button exitButton = new Button("Exit Game");
		exitButton.setPrefSize(460,60);
		exitButton.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 16;");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				boolean confirmation = ConfirmBox.displayConfirm("Exit confirmation", "Are you sure "
						+ "you want to exit? (Don't worry your progress will be saved)");
				if (confirmation) {
					_gameWindow.close();
//					exitGame();
				}
			}
		});
		
		VBox menuLayout = new VBox();
		menuLayout.setSpacing(10);
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.setPadding(new Insets(20, 20, 30, 20)); 
		menuLayout.getChildren().addAll(menuTitleText, menuInfo, gamesModuleButton, practiceModuleButton, exitButton);

		_menuScene = new Scene(menuLayout, 700, 450);
		practiceModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				PracticeModule module = new PracticeModule(_gameWindow, _questions, _menuScene);
				module.practiceModule();
			}
		});
		primaryStage.setScene(_menuScene);
		primaryStage.show();
		
		
	}
	public static void main(String[] args) {
		launch(args);		
	}
//	private void exitGame() {
//
//		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "save_data";
//		Path pathCategoryData = Paths.get(save_loc + System.getProperty("file.separator") + "categories");
//
//		try {
//			Files.createDirectories(pathCategoryData);
//		} catch (IOException e) {
//			System.err.println("Failed to create directory!" + e.getMessage());
//		}
//
////		try {
////			FileWriter winningsWriter = new FileWriter(save_loc + "/winnings");
////			winningsWriter.write(String.valueOf((_winnings)));
////			winningsWriter.close();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//
//		//Write data for each question to its respective category file
//		for (Category c : _questions.getCategoryList()) {
//			try {
//				FileWriter categoryWriter = new FileWriter(save_loc + "/categories/" + c.getCategoryName());
//
//				for (Question q : c.getQuestions()) {
//					categoryWriter.write(q.getQuestion() + "," + q.getClue() + "," + q.getAnswer() + "\n");
//				}				
//				categoryWriter.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		_gameWindow.close();
//	}
}
